package com.zjsu.syt.course;

import com.zjsu.syt.course.model.Course;
import com.zjsu.syt.course.model.Instructor;
import com.zjsu.syt.course.model.ScheduleSlot;
import com.zjsu.syt.course.model.Student;
import com.zjsu.syt.course.service.CourseService;
import com.zjsu.syt.course.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;  // 修改这里：使用jakarta包
import java.time.LocalTime;

@SpringBootApplication
public class CourseApplication {

	@Autowired
	private CourseService courseService;  // 修正字段名：去掉多余的's'

	@Autowired
	private StudentService studentService;

	@Autowired
	private Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(CourseApplication.class, args);
	}

	@PostConstruct
	public void initData() {
		// 初始化测试课程
		try {
			Instructor instructor1 = new Instructor("T001", "张教授", "zhang@example.edu.cn");
			ScheduleSlot schedule1 = new ScheduleSlot("MONDAY", LocalTime.of(8, 0), LocalTime.of(10, 0), 50);

			Course course1 = new Course();
			course1.setCode("CS101");
			course1.setTitle("计算机科学导论");
			course1.setInstructor(instructor1);
			course1.setSchedule(schedule1);
			course1.setCapacity(60);
			courseService.createCourse(course1);

			// 初始化测试学生
			Student student1 = new Student();
			student1.setStudentId("S2024001");
			student1.setName("张三");
			student1.setMajor("计算机科学与技术");
			student1.setGrade(2024);
			student1.setEmail("zhangsan@example.edu.cn");
			studentService.createStudent(student1);

			// 显示访问信息
			displayAccessInfo();

		} catch (Exception e) {
			System.out.println("初始化测试数据时发生错误: " + e.getMessage());
			e.printStackTrace();  // 添加详细错误信息
		}
	}

	private void displayAccessInfo() {
		String port = environment.getProperty("server.port", "8080");
		String contextPath = environment.getProperty("server.servlet.context-path", "");

		System.out.println("\n==========================================");
		System.out.println("🚀 校园选课系统启动成功!");
		System.out.println("==========================================");
		System.out.println("📊 访问地址: http://localhost:" + port + contextPath);
		System.out.println("📚 API 文档:");
		System.out.println("   课程管理: http://localhost:" + port + contextPath + "/api/courses");
		System.out.println("   学生管理: http://localhost:" + port + contextPath + "/api/students");
		System.out.println("   选课管理: http://localhost:" + port + contextPath + "/api/enrollments");
		System.out.println("==========================================");
		System.out.println("💡 使用 Postman 或浏览器测试 API");
		System.out.println("==========================================\n");
	}
}