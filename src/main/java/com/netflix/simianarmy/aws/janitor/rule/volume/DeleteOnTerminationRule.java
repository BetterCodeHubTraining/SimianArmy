/*
 *
 *  Copyright 2012 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.simianarmy.aws.janitor.rule.volume;

import com.netflix.simianarmy.MonkeyCalendar;
import com.netflix.simianarmy.Resource;
import com.netflix.simianarmy.aws.janitor.crawler.edda.EddaEBSVolumeJanitorCrawler;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * The rule is for checking whether an EBS volume is not attached to any instance and had the
 * DeleteOnTermination flag set in the previous attachment. This is an error case that AWS didn't
 * handle. The volume should have been deleted as soon as it was detached.
 *
 * NOTE: since the information came from the history, the rule will work only if Edda is enabled
 * for Janitor Monkey.
 */
public class DeleteOnTerminationRule extends VolumeRule  {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteOnTerminationRule.class);

    private final MonkeyCalendar calendar;

    private final int retentionDays;

    /**
     * The termination reason for the DeleteOnTerminationRule.
     */
    public static final String TERMINATION_REASON = "Not attached and DeleteOnTerminate flag was set";

    /**
     * Constructor.
     *
     * @param calendar
     *            The calendar used to calculate the termination time
     * @param retentionDays
     *            The number of days that the volume is retained before being terminated after being marked
     *            as cleanup candidate
     */
    public DeleteOnTerminationRule(MonkeyCalendar calendar, int retentionDays) {
        Validate.notNull(calendar);
        Validate.isTrue(retentionDays >= 0);
        this.calendar = calendar;
        this.retentionDays = retentionDays;
    }


    @Override
    public boolean isValidResource(Resource resource) {
        if ("true".equals(resource.getAdditionalField(EddaEBSVolumeJanitorCrawler.DELETE_ON_TERMINATION))) {
            if (resource.getExpectedTerminationTime() == null) {
                Date terminationTime = calendar.getBusinessDay(calendar.now().getTime(), retentionDays);
                resource.setExpectedTerminationTime(terminationTime);
                resource.setTerminationReason(TERMINATION_REASON);
                LOGGER.info(String.format(
                        "Volume %s is marked to be cleaned at %s as it is detached and DeleteOnTermination was set",
                        resource.getId(), resource.getExpectedTerminationTime()));
            } else {
                LOGGER.info(String.format("Resource %s is already marked.", resource.getId()));
            }
            return false;
        }
        return true;
    }
}
