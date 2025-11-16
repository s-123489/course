#!/bin/bash
echo "=== 开始 Docker 容器测试 ==="
sleep 30
echo "1. 测试应用健康状态..."
curl -f http://localhost:8080/actuator/health || exit 1
echo "2. 测试课程API..."
curl -X GET http://localhost:8080/api/courses
echo "=== 基础测试完成 ==="
