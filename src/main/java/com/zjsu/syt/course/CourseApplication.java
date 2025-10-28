package com.zjsu.syt.course;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
public class CourseApplication {

	public static void main(String[] args) {
		try {
			SpringApplication app = new SpringApplication(CourseApplication.class);
			Environment env = app.run(args).getEnvironment();

			String port = env.getProperty("server.port", "8080");
			String appName = env.getProperty("spring.application.name", "course-management-system");

			log.info("\n" +
							"========================================\n" +
							"应用启动成功!\n" +
							"应用名称: {}\n" +
							"本地访问: http://localhost:{}\n" +
							"API状态: http://localhost:{}/api/status\n" +
							"健康检查: http://localhost:{}/health/db\n" +
							"H2控制台: http://localhost:{}/h2-console\n" +
							"========================================",
					appName, port, port, port, port);

		} catch (Exception e) {
			log.error("应用启动失败: {}", e.getMessage(), e);
		}
	}
}