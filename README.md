# Coins

Java Version
---
1.8


Database Connect
---
The default username/password is root/root
If you want to modify, change it in config.yml


如何启动项目
---

1. 执行 `mvn clean install` 来build项目
2. 执行script下面的demo-project.sh脚本. ./demo-project.sh start
3. 通过访问 `http://localhost:8080`来检查项目是否启动


要创建的数据库
---
``` sql
CREATE TABLE `coin_hist` (
    `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`type_id` TINYINT(4) NOT NULL,
	`from_user` BIGINT(20) NULL DEFAULT NULL,
	`to_user` BIGINT(20) NULL DEFAULT NULL,
	`coin_num` INT(11) NOT NULL,
	`created_on` INT(10) NOT NULL,
	`updated_on` INT(10) NOT NULL,
	PRIMARY KEY (`id`)
)

CREATE TABLE `user_coin` (
	`id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT(20) NOT NULL,
	`coins` INT(11) NOT NULL,
	`created_on` INT(10) NOT NULL,
	`updated_on` INT(10) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `i1` (`user_id`)
)
```

API种类
---
1. 查看用户余额

        path: /coins/user/{user_id}

        param: 在uri里userid指代要查询用户的id

        返回结果样式:  {"userId": 1,"coins": "1"}

2. 给用户充值

        path: /user/add

        param:
            a.user_id query_param 需要充值的user_id
            b.coins query_param   需要充值的数目

        返回结果样式:  {"userId": 1,"coins": "1"}

3. 用户间转账

        path: /transaction/transfer

        param:
            a.from_user_id query_param  转走coin的用户id
            b.to_user_id query_param    收到coin的用户id
            c.coins query_param  转账的数目


查看thread的stack
---
通过访问http://localhost:8081/threads
