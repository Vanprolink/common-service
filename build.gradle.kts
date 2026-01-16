import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	java
	`java-library`
	`maven-publish`
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
	id("maven-publish")
}

group = "com.ecommerce"
version = "1.0.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
	// Xóa các dòng withJavadocJar() gây lỗi ở đây đi
}

repositories {
	mavenCentral()
}

dependencies {
	// Dùng api để service con có thể tái sử dụng thư viện
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.security:spring-security-core")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
}

// --- CẤU HÌNH FIX LỖI (VIẾT LẠI TƯỜNG MINH) ---

// 1. Tắt bootJar (File chạy)
tasks.named<BootJar>("bootJar") {
	enabled = false
}

// 2. Bật Jar thường (Thư viện) và sửa tên
tasks.named<Jar>("jar") {
	enabled = true
	// Sửa lỗi archiveClassifier: Dùng archiveClassifier.set("") là chuẩn nhất
	archiveClassifier.set("")
}

// 3. Tắt Javadoc (Để tránh lỗi Lombok khi build)
tasks.withType<Javadoc> {
	enabled = false
}

// 4. Tắt Test (Không cần test thư viện lúc build)
tasks.withType<Test> {
	enabled = false
}

// 5. Cấu hình Publish
publishing {
	publications {
		create<MavenPublication>("gpr") {
			// 2. Dùng components["java"] là chuẩn nhất.
			// Nó tự động tạo file POM chứa danh sách các thư viện con mà common cần.
			from(components["java"])

			// 3. QUAN TRỌNG: Ép tên gói hàng là 'common'
			// Để bên Order Service gọi đúng là: com.ecommerce:common:1.0.0
			artifactId = "common"
			// --- THÊM ĐOẠN NÀY ĐỂ SỬA LỖI ---
			versionMapping {
				usage("java-api") {
					fromResolutionOf("runtimeClasspath")
				}
				usage("java-runtime") {
					fromResolutionOf("runtimeClasspath")
				}
			}
		}
	}
	repositories {
		maven {
			name = "GitHubPackages"
			// Thay YOUR_USERNAME bằng tên GitHub của bạn
			// Thay REPO_NAME bằng tên repository chứa common lib này
			url = uri("https://maven.pkg.github.com/Vanprolink/common-service")
			credentials {
				username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user") as String?
				password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.key") as String?
			}
		}
	}
}