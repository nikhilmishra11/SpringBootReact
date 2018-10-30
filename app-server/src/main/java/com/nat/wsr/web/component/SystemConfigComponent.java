package com.nat.wsr.web.component;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SystemConfigComponent {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static String TEMP_PATH = "/";
	@EventListener(ContextRefreshedEvent.class)
	public void contextRefreshedEvent() {
		String rootPath = System.getProperty("catalina.home");
		String path1 = rootPath + File.separator + "temp";
		File tempdir = new File(path1);
		logger.info("Temp directory {}", tempdir);
		if (!tempdir.exists()) {
			tempdir.mkdirs();
		}
		TEMP_PATH = path1;
	}
}