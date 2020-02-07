package eu.europa.ec.joinup.tsl.business.util;

import java.util.Date;

import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;

public class CronUtils {

    public static Date getDateFromExpression(final Date checkDateb, String cronExpression) {
        CronTrigger trigger = new CronTrigger(cronExpression);
        TriggerContext context = new TriggerContext() {

            @Override
            public Date lastScheduledExecutionTime() {
                return checkDateb;
            }

            @Override
            public Date lastActualExecutionTime() {
                return checkDateb;
            }

            @Override
            public Date lastCompletionTime() {
                return checkDateb;
            }
        };
        Date nextExecution = trigger.nextExecutionTime(context);
        return nextExecution;
    }
}
