package grails.plugin.quartzconfigschedular

import grails.core.GrailsApplication
import groovy.util.logging.Slf4j
import org.quartz.Scheduler
import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringApplicationRunListener
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.ConfigurableEnvironment

/**
 * Spring application run listener to schedule jobs defined in config.
 */

@Slf4j
class QuartzRunListener implements SpringApplicationRunListener {

	public QuartzRunListener(SpringApplication application, String[] args) { }

	@Override
	void started() { }

	@Override
	void environmentPrepared(ConfigurableEnvironment environment) { }

	@Override
	void contextPrepared(ConfigurableApplicationContext context) { }

	@Override
	void contextLoaded(ConfigurableApplicationContext context) { }

	@Override
	void finished(ConfigurableApplicationContext context, Throwable exception) {
		if(!context) return
		GrailsApplication grailsApplication = context.getBean("grailsApplication")
		Scheduler scheduler = context.getBean('quartzScheduler')
		if(grailsApplication.config.grails.plugin.quartz.autoStartup == true){
			log.info("grails.plugin.quartz.autoStartup is true, scheduling jobs")
			def builders = grailsApplication.config.grails.plugin.quartz.jobSetup
			if(builders?.keySet()){
				builders.each{key,clos->
					log.info("Scheduling job $key")
					clos(scheduler, context)
				}
			}
		}
	}
}
