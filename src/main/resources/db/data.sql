-- 初始化课程数据
INSERT INTO courses (id, code, title, instructor_id, instructor_name, instructor_email, day_of_week, start_time, end_time, expected_attendance, capacity, enrolled, created_at) VALUES
('course-001', 'CS101', '计算机科学导论', 'T001', '张教授', 'zhang@example.edu.cn', 'MONDAY', '08:00:00', '10:00:00', 50, 60, 0, NOW()),
('course-002', 'MATH201', '高等数学', 'T002', '李教授', 'li@example.edu.cn', 'TUESDAY', '10:00:00', '12:00:00', 40, 50, 0, NOW()),
('course-003', 'ENG101', '大学英语', 'T003', '王老师', 'wang@example.edu.cn', 'WEDNESDAY', '14:00:00', '16:00:00', 30, 40, 0, NOW());

-- 初始化学生数据
INSERT INTO students (id, student_id, name, major, grade, email, created_at) VALUES
('student-001', 'S2024001', '张三', '计算机科学与技术', 2024, 'zhangsan@example.edu.cn', NOW()),
('student-002', 'S2024002', '李四', '软件工程', 2024, 'lisi@example.edu.cn', NOW()),
('student-003', 'S2024003', '王五', '数据科学', 2024, 'wangwu@example.edu.cn', NOW());

-- 初始化选课数据
INSERT INTO enrollments (id, course_id, student_id, status, enrolled_at) VALUES
('enroll-001', 'course-001', 'S2024001', 'ACTIVE', NOW()),
('enroll-002', 'course-002', 'S2024002', 'ACTIVE', NOW());