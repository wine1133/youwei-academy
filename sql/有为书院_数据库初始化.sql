-- 有为书院 · 智慧阅读与学习平台
-- MySQL 8.0 / utf8mb4
CREATE DATABASE IF NOT EXISTS `free_system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `free_system`;
CREATE TABLE IF NOT EXISTS `admin` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) DEFAULT NULL COMMENT '账号',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `role` varchar(255) DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员信息';

CREATE TABLE IF NOT EXISTS `teacher` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) DEFAULT NULL COMMENT '账号',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `role` varchar(255) DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教师信息';

CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) DEFAULT NULL COMMENT '账号',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `role` varchar(255) DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息';

CREATE TABLE IF NOT EXISTS `book` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(200) NOT NULL COMMENT '书名',
  `author` varchar(120) DEFAULT NULL COMMENT '作者',
  `cover` varchar(500) DEFAULT NULL COMMENT '封面',
  `category` varchar(80) DEFAULT NULL COMMENT '分类',
  `publisher` varchar(200) DEFAULT NULL COMMENT '出版社',
  `isbn` varchar(60) DEFAULT NULL COMMENT 'ISBN',
  `description` text COMMENT '简介',
  `total` int NOT NULL DEFAULT 10 COMMENT '馆藏数量',
  `stock` int NOT NULL DEFAULT 10 COMMENT '可借数量',
  `score` decimal(3,1) NOT NULL DEFAULT 5.0 COMMENT '评分',
  `status` varchar(20) NOT NULL DEFAULT 'AVAILABLE' COMMENT '状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_book_category` (`category`),
  KEY `idx_book_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图书信息';

CREATE TABLE IF NOT EXISTS `chapter` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_id` int NOT NULL,
  `title` varchar(200) NOT NULL,
  `sort_no` int NOT NULL DEFAULT 1,
  `content` longtext,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_chapter_book` (`book_id`, `sort_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图书章节';

CREATE TABLE IF NOT EXISTS `borrow` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_id` int NOT NULL,
  `user_id` int NOT NULL,
  `borrow_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `due_time` datetime DEFAULT NULL,
  `return_time` datetime DEFAULT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'BORROWED',
  PRIMARY KEY (`id`),
  KEY `idx_borrow_book` (`book_id`),
  KEY `idx_borrow_user` (`user_id`),
  KEY `idx_borrow_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='借阅记录';

CREATE TABLE IF NOT EXISTS `article` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(240) NOT NULL,
  `summary` varchar(500) DEFAULT NULL,
  `cover` varchar(500) DEFAULT NULL,
  `content` longtext,
  `category` varchar(80) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `views` int NOT NULL DEFAULT 0,
  `likes` int NOT NULL DEFAULT 0,
  `collects` int NOT NULL DEFAULT 0,
  `status` varchar(20) NOT NULL DEFAULT 'PUBLISHED',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_article_user` (`user_id`),
  KEY `idx_article_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识文章';

CREATE TABLE IF NOT EXISTS `praise` (
  `id` int NOT NULL AUTO_INCREMENT,
  `article_id` int NOT NULL,
  `user_id` int NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_praise_user_article` (`user_id`, `article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章点赞';

CREATE TABLE IF NOT EXISTS `collect` (
  `id` int NOT NULL AUTO_INCREMENT,
  `article_id` int NOT NULL,
  `user_id` int NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_collect_user_article` (`user_id`, `article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章收藏';

CREATE TABLE IF NOT EXISTS `comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `article_id` int NOT NULL,
  `user_id` int NOT NULL,
  `content` varchar(1000) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_comments_article` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章评论';

CREATE TABLE IF NOT EXISTS `homework` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(240) NOT NULL,
  `content` text,
  `file` varchar(500) DEFAULT NULL,
  `teacher_id` int DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_homework_teacher` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程作业';

CREATE TABLE IF NOT EXISTS `submit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `homework_id` int NOT NULL,
  `user_id` int NOT NULL,
  `file` varchar(500) DEFAULT NULL,
  `content` text,
  `score` decimal(5,1) DEFAULT NULL,
  `comment` varchar(1000) DEFAULT NULL,
  `submit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(20) NOT NULL DEFAULT 'SUBMITTED',
  PRIMARY KEY (`id`),
  KEY `idx_submit_homework` (`homework_id`),
  KEY `idx_submit_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='作业提交';
CREATE TABLE IF NOT EXISTS `space` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(200) NOT NULL COMMENT '空间名称',
  `address` varchar(300) DEFAULT NULL COMMENT '地址',
  `longitude` decimal(12,8) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(12,8) DEFAULT NULL COMMENT '纬度',
  `description` varchar(800) DEFAULT NULL COMMENT '空间介绍',
  `open_time` varchar(100) DEFAULT NULL COMMENT '开放时间',
  `cover` varchar(500) DEFAULT NULL COMMENT '封面',
  `status` varchar(20) NOT NULL DEFAULT 'OPEN' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='书院空间与地图地点';

CREATE TABLE IF NOT EXISTS `message` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '接收人ID',
  `role` varchar(20) NOT NULL COMMENT '接收人角色',
  `title` varchar(200) NOT NULL COMMENT '消息标题',
  `content` varchar(1000) DEFAULT NULL COMMENT '消息内容',
  `type` varchar(40) NOT NULL DEFAULT 'SYSTEM' COMMENT '消息类型',
  `related_id` int DEFAULT NULL COMMENT '关联业务ID',
  `is_read` tinyint NOT NULL DEFAULT 0 COMMENT '0未读 1已读',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_message_receiver` (`user_id`,`role`,`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息通知';

INSERT INTO `admin` (`id`,`username`,`password`,`name`,`avatar`,`role`) VALUES
(1,'admin','admin','管理员','/images/avatars/admin.jpg','ADMIN')
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`),`avatar`=VALUES(`avatar`),`role`='ADMIN';
INSERT INTO `teacher` (`id`,`username`,`password`,`name`,`avatar`,`role`) VALUES
(3,'teacher','123456','林知远','/images/avatars/teacher.jpg','TEACHER')
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`),`avatar`=VALUES(`avatar`),`role`='TEACHER';
INSERT INTO `user` (`id`,`username`,`password`,`name`,`avatar`,`role`) VALUES
(2,'ill','123456','沈星河','/images/avatars/student.jpg','USER'),
(4,'user','123456','苏念','/images/avatars/student2.jpg','USER')
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`),`avatar`=VALUES(`avatar`),`role`='USER';
INSERT INTO `space` (`id`,`name`,`address`,`longitude`,`latitude`,`description`,`open_time`,`cover`,`status`) VALUES
(1,'有为书院·中心馆','大连市甘井子区凌工路周边',121.53500000,38.88300000,'面向书院成员开放的阅读、自习与交流空间。','09:00 - 21:00','/images/real/spaces/space-01.jpg','OPEN'),
(2,'凌水湾城市书房','大连市甘井子区凌水湾',121.54800000,38.86900000,'临海阅读空间，适合周末安静阅读与小型分享会。','10:00 - 20:00','/images/real/spaces/space-02.jpg','OPEN'),
(3,'星海阅读空间','大连市沙河口区星海广场周边',121.58900000,38.87400000,'城市文化会客厅，提供图书借阅和主题展览。','10:00 - 22:00','/images/real/spaces/space-03.jpg','OPEN'),
(4,'大连理工大学图书馆','大连市甘井子区凌工路2号',121.52900000,38.88400000,'高校知识空间，支持学习、检索与学术交流。','08:00 - 22:00','/images/real/spaces/space-04.jpg','OPEN')
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`),`address`=VALUES(`address`),`longitude`=VALUES(`longitude`),`latitude`=VALUES(`latitude`),`open_time`=VALUES(`open_time`),`status`=VALUES(`status`);

INSERT INTO `message` (`id`,`user_id`,`role`,`title`,`content`,`type`,`related_id`,`is_read`) VALUES
(1,1,'ADMIN','欢迎进入有为书院管理端','馆藏、借阅、文章与学习任务数据已准备就绪。','SYSTEM',NULL,0),
(2,3,'TEACHER','教学工作台已准备好','你可以发布作业、查看提交并给出分数与评语。','SYSTEM',NULL,0),
(3,2,'USER','欢迎来到有为书院','从一本好书开始，去阅读、思考并与伙伴分享。','SYSTEM',NULL,0),
(4,4,'USER','欢迎来到有为书院','馆藏与思想社区已经为你开放。','SYSTEM',NULL,0)
ON DUPLICATE KEY UPDATE `title`=VALUES(`title`),`content`=VALUES(`content`),`type`=VALUES(`type`),`role`=VALUES(`role`);

INSERT INTO `book` (`id`,`name`,`author`,`cover`,`category`,`publisher`,`isbn`,`description`,`total`,`stock`,`score`) VALUES
(1,'百年孤独','加西亚·马尔克斯','/images/real/books/book-01.jpg','文学','南海出版公司','9787544253994','一部关于家族、记忆与时间循环的魔幻现实主义经典。',18,14,9.3),
(2,'人类简史','尤瓦尔·赫拉利','/images/real/books/book-02.jpg','历史','中信出版社','9787508647357','从认知革命到科技革命，重新理解人类如何成为世界的主人。',12,9,9.1),
(3,'深入理解计算机系统','Randal E. Bryant','/images/real/books/book-03.png','计算机','机械工业出版社','9787111544937','从程序员视角理解计算机系统，是计算机专业必读经典。',10,7,9.8),
(4,'月亮与六便士','毛姆','/images/real/books/book-04.jpg','文学','中国友谊出版公司','9787505738720','在满地都是六便士的街上，他抬头看见了月亮。',15,12,9.0),
(5,'设计中的设计','原研哉','/images/real/books/book-05.jpg','设计','山东人民出版社','9787209041065','从日常生活出发，重新思考设计的本质与边界。',8,6,8.8),
(6,'活着','余华','/images/real/books/book-06.jpg','文学','作家出版社','9787506365437','以朴素而有力的叙述，写尽生命的苦难与韧性。',20,16,9.4),
(7,'代码整洁之道','Robert C. Martin','/images/real/books/book-07.jpg','计算机','人民邮电出版社','9787115216878','如何写出可读、可维护、值得长期演进的高质量代码。',9,5,9.1),
(8,'艺术的故事','贡布里希','/images/real/books/book-08.jpg','艺术','广西美术出版社','9787549401929','一部雅俗共赏的西方艺术史入门经典。',7,5,9.6)
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`),`author`=VALUES(`author`),`cover`=VALUES(`cover`),`description`=VALUES(`description`),`total`=VALUES(`total`),`stock`=VALUES(`stock`),`score`=VALUES(`score`);

INSERT INTO `chapter` (`id`,`book_id`,`title`,`sort_no`,`content`) VALUES
(1,1,'第一章 冰块与吉普赛人',1,'多年以后，面对行刑队，奥雷里亚诺·布恩迪亚上校将会回想起父亲带他去见识冰块的那个遥远的下午。那时的马孔多是一个二十户人家的村落，泥巴和芦苇盖成的屋子沿河岸排开。'),
(2,1,'第二章 马孔多的新生',2,'吉普赛人梅尔基亚德斯带来了磁铁、放大镜和炼金术。何塞·阿尔卡蒂奥·布恩迪亚对未知事物充满狂热，他相信世界还有许多尚未被发现的神奇。'),
(3,2,'第一部分 认知革命',1,'大约七万年前，智人开始出现新的思维和沟通方式。虚构故事的能力让大规模陌生人协作成为可能，也让人类走出非洲，走向整个世界。'),
(4,2,'第二部分 农业革命',2,'农业革命提高了总产量，却未必改善个体生活。人类被小麦、田地和定居生活重新塑造，历史的进步与个体的幸福并不总是同向。'),
(5,3,'第一章 计算机系统漫游',1,'计算机系统由硬件和软件组成。一个简单的 hello 程序，背后经历了预处理、编译、汇编、链接、加载和运行等多个阶段。'),
(6,3,'第二章 信息的表示和处理',2,'计算机用有限的比特表示信息。理解无符号数、补码、浮点数和溢出，是写出可靠程序的基础。')
ON DUPLICATE KEY UPDATE `title`=VALUES(`title`),`content`=VALUES(`content`);

INSERT INTO `article` (`id`,`title`,`summary`,`cover`,`content`,`category`,`tags`,`user_id`,`views`,`likes`,`collects`) VALUES
(1,'在数字时代，重新发现深度阅读的力量','当信息越来越快，我们更需要一种慢下来、与思想真正相遇的能力。','/images/real/articles/article-01.jpg','<p>数字技术让获取信息变得前所未有的容易，但阅读并没有因此变得更轻松。真正困难的，是在海量信息中保持专注，在快速滑动之间停下来思考。</p><p>深度阅读不是把一本书从头到尾翻完，而是与作者建立持续的对话。做笔记、提出问题、回看自己的经验，都是让阅读进入生命的方式。</p><blockquote>阅读的意义，不是知道更多，而是成为更完整的人。</blockquote><p>每天留出二十分钟，关掉通知，读几页真正值得读的书。长期来看，这会成为你最重要的精神资产。</p>','阅读方法','深度阅读,学习方法',3,1286,96,58),
(2,'从零开始搭建你的知识管理系统','不是收藏更多，而是让知识真正参与你的思考与创造。','/images/real/articles/article-02.jpg','<p>知识管理的核心不是工具，而是输入、连接和输出。收藏只是输入，整理只是连接，只有真正写出来、用起来，知识才属于你。</p><p>可以从一个最小系统开始：每天记录一个问题，每周整理一次笔记，每月输出一篇总结。让知识流动起来，而不是沉睡在文件夹里。</p>','效率工具','知识管理,笔记',1,864,72,41),
(3,'2026 春季书单：给忙碌生活留一盏灯','八本书，八种理解世界的方式。','/images/real/articles/article-03.jpg','<p>春天适合重新开始，也适合安静地读一本书。这份书单没有严格的排名，只希望其中某一本，能在恰好的时刻与你相遇。</p><p>从《百年孤独》的时间迷宫，到《设计中的设计》的日常观察；从《人类简史》的宏大叙事，到《活着》的生命韧性，每一本书都是一扇窗。</p>','主题书单','书单,阅读',4,2031,154,103),
(4,'教师手记：如何让阅读真正进入课堂','阅读不应只是任务，而应成为学生理解世界的方式。','/images/real/articles/article-04.jpg','<p>在课堂上，我们常常急于让学生总结主题、提炼意义，却忽略了阅读首先是一种体验。让学生先说出困惑、惊讶和不同意，往往比标准答案更重要。</p><p>把阅读与写作、讨论、项目式学习连接起来，学生才会发现知识不是孤立的，而是可以解释现实、改变生活的。</p>','教学随笔','阅读教学,课堂',3,642,53,29)
ON DUPLICATE KEY UPDATE `title`=VALUES(`title`),`summary`=VALUES(`summary`),`content`=VALUES(`content`);

INSERT INTO `homework` (`id`,`title`,`content`,`teacher_id`,`deadline`) VALUES
(1,'《人类简史》读书报告','结合课堂讨论，选择一个你最有共鸣的章节，写一篇不少于 1200 字的读书报告。要求有自己的问题意识和现实联系。',3,DATE_ADD(NOW(), INTERVAL 14 DAY)),
(2,'计算机系统课程设计：缓存模拟器','实现一个支持 LRU 替换策略的缓存模拟器，记录命中率、缺失率与访问轨迹，并提交源码和设计说明。',3,DATE_ADD(NOW(), INTERVAL 21 DAY))
ON DUPLICATE KEY UPDATE `title`=VALUES(`title`),`content`=VALUES(`content`),`deadline`=VALUES(`deadline`);

INSERT INTO `borrow` (`id`,`book_id`,`user_id`,`borrow_time`,`due_time`,`return_time`,`status`) VALUES
(1,1,2,DATE_SUB(NOW(), INTERVAL 3 DAY),DATE_ADD(NOW(), INTERVAL 11 DAY),NULL,'BORROWED'),
(2,3,4,DATE_SUB(NOW(), INTERVAL 20 DAY),DATE_SUB(NOW(), INTERVAL 6 DAY),DATE_SUB(NOW(), INTERVAL 8 DAY),'RETURNED'),
(3,4,2,DATE_SUB(NOW(), INTERVAL 9 DAY),DATE_ADD(NOW(), INTERVAL 5 DAY),NULL,'BORROWED')
ON DUPLICATE KEY UPDATE `status`=VALUES(`status`),`due_time`=VALUES(`due_time`),`return_time`=VALUES(`return_time`);

INSERT INTO `comments` (`id`,`article_id`,`user_id`,`content`) VALUES
(1,1,2,'很喜欢“阅读不是知道更多，而是成为更完整的人”这句话。'),
(2,1,4,'最近也在尝试每天固定阅读二十分钟，确实比刷短视频更充实。'),
(3,4,2,'老师提到的“先说不懂”很有启发，阅读课上我们常常太急着找答案。')
ON DUPLICATE KEY UPDATE `content`=VALUES(`content`);

INSERT INTO `praise` (`id`,`article_id`,`user_id`) VALUES (1,1,2),(2,3,2),(3,4,2)
ON DUPLICATE KEY UPDATE `article_id`=VALUES(`article_id`);
INSERT INTO `collect` (`id`,`article_id`,`user_id`) VALUES (1,1,2),(2,2,2)
ON DUPLICATE KEY UPDATE `article_id`=VALUES(`article_id`);

INSERT INTO `borrow` (`id`,`book_id`,`user_id`,`borrow_time`,`due_time`,`return_time`,`status`) VALUES
(4,2,2,DATE_SUB(NOW(), INTERVAL 1 DAY),DATE_ADD(NOW(), INTERVAL 29 DAY),DATE_SUB(NOW(), INTERVAL 1 DAY),'RETURNED'),
(5,3,4,DATE_SUB(NOW(), INTERVAL 2 DAY),DATE_ADD(NOW(), INTERVAL 28 DAY),DATE_SUB(NOW(), INTERVAL 2 DAY),'RETURNED'),
(6,5,4,DATE_SUB(NOW(), INTERVAL 3 DAY),DATE_ADD(NOW(), INTERVAL 27 DAY),DATE_SUB(NOW(), INTERVAL 3 DAY),'RETURNED'),
(7,6,2,DATE_SUB(NOW(), INTERVAL 4 DAY),DATE_ADD(NOW(), INTERVAL 26 DAY),DATE_SUB(NOW(), INTERVAL 4 DAY),'RETURNED'),
(8,7,4,DATE_SUB(NOW(), INTERVAL 4 DAY),DATE_ADD(NOW(), INTERVAL 26 DAY),DATE_SUB(NOW(), INTERVAL 4 DAY),'RETURNED'),
(9,8,2,DATE_SUB(NOW(), INTERVAL 5 DAY),DATE_ADD(NOW(), INTERVAL 25 DAY),DATE_SUB(NOW(), INTERVAL 5 DAY),'RETURNED'),
(10,2,4,DATE_SUB(NOW(), INTERVAL 6 DAY),DATE_ADD(NOW(), INTERVAL 24 DAY),DATE_SUB(NOW(), INTERVAL 6 DAY),'RETURNED'),
(11,6,2,DATE_SUB(NOW(), INTERVAL 6 DAY),DATE_ADD(NOW(), INTERVAL 24 DAY),DATE_SUB(NOW(), INTERVAL 6 DAY),'RETURNED')
ON DUPLICATE KEY UPDATE `status`=VALUES(`status`),`borrow_time`=VALUES(`borrow_time`),`due_time`=VALUES(`due_time`),`return_time`=VALUES(`return_time`);
