CREATE TABLE `sys_region`
(
    `id`                 int(11)     NOT NULL AUTO_INCREMENT COMMENT '地区Id',
    `code`               char(6)     NOT NULL COMMENT '地区编码',
    `name`               varchar(20) NOT NULL COMMENT '地区名',
    `level`              tinyint(1)  NOT NULL DEFAULT '0' COMMENT '地区级别（1:省份province,2:市city,3:区县district,4:街道street）',
    `city_code`          char(4)              DEFAULT NULL COMMENT '城市编码',
    `lng`                varchar(20)          DEFAULT '0' COMMENT '城市中心经度',
    `lat`                varchar(20)          DEFAULT '0' COMMENT '城市中心纬度',
    `parent_id`          int(11)     NOT NULL DEFAULT '-1' COMMENT '地区父节点',
    `create_time`        TIMESTAMP            DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_modified_time` TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `regionCode` (`code`),
    KEY `parentId` (`parent_id`),
    KEY `level` (`level`),
    KEY `regionName` (`name`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
    comment '地区表'
  ROW_FORMAT = Dynamic;
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1, '100000', '中华人民共和国', 0, '', 116.3683244, 39.915085, 0);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2, '230000', '黑龙江省', 1, '', 126.642464, 45.756967, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3, '230900', '七台河市', 2, '0464', 131.015584, 45.771266, 2);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (4, '230903', '桃山区', 3, '0464', 131.015848, 45.771217, 3);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (5, '230921', '勃利县', 3, '0464', 130.575025, 45.751573, 3);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (6, '230902', '新兴区', 3, '0464', 130.889482, 45.794258, 3);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (7, '230904', '茄子河区', 3, '0464', 131.071561, 45.776587, 3);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (8, '230400', '鹤岗市', 2, '0468', 130.277487, 47.332085, 2);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (9, '230422', '绥滨县', 3, '0468', 131.860526, 47.289892, 8);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (10, '230403', '工农区', 3, '0468', 130.276652, 47.331678, 8);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (11, '230407', '兴山区', 3, '0468', 130.30534, 47.35997, 8);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (12, '230402', '向阳区', 3, '0468', 130.292478, 47.345372, 8);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (13, '230404', '南山区', 3, '0468', 130.275533, 47.31324, 8);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (14, '230421', '萝北县', 3, '0468', 130.829087, 47.577577, 8);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (15, '230405', '兴安区', 3, '0468', 130.236169, 47.252911, 8);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (16, '230406', '东山区', 3, '0468', 130.31714, 47.337385, 8);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (17, '230700', '伊春市', 2, '0458', 128.899396, 47.724775, 2);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (18, '230722', '嘉荫县', 3, '0458', 130.397684, 48.891378, 17);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (19, '230726', '南岔县', 3, '0458', 129.28246, 47.137314, 17);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (20, '230718', '乌翠区', 3, '0458', 128.669859, 47.726728, 17);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (21, '230717', '伊美区', 3, '0458', 128.907303, 47.728171, 17);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (22, '230719', '友好区', 3, '0458', 128.84075, 47.853778, 17);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (23, '230751', '金林区', 3, '0458', 129.429117, 47.413074, 17);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (24, '230723', '汤旺县', 3, '0458', 129.571108, 48.454651, 17);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (25, '230724', '丰林县', 3, '0458', 129.5336, 48.290455, 17);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (26, '230781', '铁力市', 3, '0458', 128.030561, 46.985772, 17);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (27, '230725', '大箐山县', 3, '0458', 129.020793, 47.028397, 17);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (28, '232700', '大兴安岭地区', 2, '0457', 124.711526, 52.335262, 2);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (29, '232701', '漠河市', 3, '0457', 122.536256, 52.972074, 28);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (30, '232722', '塔河县', 3, '0457', 124.710516, 52.335229, 28);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (31, '232721', '呼玛县', 3, '0457', 126.662105, 51.726998, 28);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (32, '232718', '加格达奇区', 3, '0457', 124.126716, 50.424654, 28);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (33, '231200', '绥化市', 2, '0455', 126.99293, 46.637393, 2);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (34, '231226', '绥棱县', 3, '0455', 127.111121, 47.247195, 33);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (35, '231283', '海伦市', 3, '0455', 126.969383, 47.460428, 33);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (36, '231224', '庆安县', 3, '0455', 127.510024, 46.879203, 33);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (37, '231221', '望奎县', 3, '0455', 126.484191, 46.83352, 33);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (38, '231202', '北林区', 3, '0455', 126.990665, 46.634912, 33);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (39, '231223', '青冈县', 3, '0455', 126.112268, 46.686596, 33);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (40, '231222', '兰西县', 3, '0455', 126.289315, 46.259037, 33);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (41, '231225', '明水县', 3, '0455', 125.907544, 47.183527, 33);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (42, '231282', '肇东市', 3, '0455', 125.991402, 46.069471, 33);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (43, '231281', '安达市', 3, '0455', 125.329926, 46.410614, 33);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (44, '230100', '哈尔滨市', 2, '0451', 126.642464, 45.756967, 2);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (45, '230126', '巴彦县', 3, '0451', 127.403602, 46.081889, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (46, '230123', '依兰县', 3, '0451', 129.565594, 46.315105, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (47, '230128', '通河县', 3, '0451', 128.747786, 45.977618, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (48, '230127', '木兰县', 3, '0451', 128.042675, 45.949826, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (49, '230125', '宾县', 3, '0451', 127.48594, 45.759369, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (50, '230124', '方正县', 3, '0451', 128.836131, 45.839536, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (51, '230108', '平房区', 3, '0451', 126.629257, 45.605567, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (52, '230129', '延寿县', 3, '0451', 128.331886, 45.455648, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (53, '230183', '尚志市', 3, '0451', 127.968539, 45.214953, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (54, '230113', '双城区', 3, '0451', 126.308784, 45.377942, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (55, '230110', '香坊区', 3, '0451', 126.667049, 45.713067, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (56, '230102', '道里区', 3, '0451', 126.612532, 45.762035, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (57, '230184', '五常市', 3, '0451', 127.15759, 44.919418, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (58, '230112', '阿城区', 3, '0451', 126.972726, 45.538372, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (59, '230111', '呼兰区', 3, '0451', 126.603302, 45.98423, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (60, '230109', '松北区', 3, '0451', 126.563066, 45.814656, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (61, '230103', '南岗区', 3, '0451', 126.652098, 45.755971, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (62, '230104', '道外区', 3, '0451', 126.648838, 45.78454, 44);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (63, '230200', '齐齐哈尔市', 2, '0452', 123.95792, 47.342081, 2);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (64, '230281', '讷河市', 3, '0452', 124.882172, 48.481133, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (65, '230225', '甘南县', 3, '0452', 123.506034, 47.917838, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (66, '230229', '克山县', 3, '0452', 125.874355, 48.034342, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (67, '230230', '克东县', 3, '0452', 126.249094, 48.03732, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (68, '230223', '依安县', 3, '0452', 125.307561, 47.890098, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (69, '230227', '富裕县', 3, '0452', 124.469106, 47.797172, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (70, '230207', '碾子山区', 3, '0452', 122.887972, 47.51401, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (71, '230204', '铁锋区', 3, '0452', 123.973555, 47.339499, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (72, '230203', '建华区', 3, '0452', 123.955888, 47.354494, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (73, '230208', '梅里斯达斡尔族区', 3, '0452', 123.754599, 47.311113, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (74, '230221', '龙江县', 3, '0452', 123.187225, 47.336388, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (75, '230202', '龙沙区', 3, '0452', 123.957338, 47.341736, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (76, '230206', '富拉尔基区', 3, '0452', 123.638873, 47.20697, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (77, '230205', '昂昂溪区', 3, '0452', 123.813181, 47.156867, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (78, '230231', '拜泉县', 3, '0452', 126.091911, 47.607363, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (79, '230224', '泰来县', 3, '0452', 123.41953, 46.39233, 63);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (80, '231100', '黑河市', 2, '0456', 127.499023, 50.249585, 2);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (81, '231183', '嫩江市', 3, '0456', 125.229904, 49.177461, 80);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (82, '231102', '爱辉区', 3, '0456', 127.497639, 50.249027, 80);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (83, '231181', '北安市', 3, '0456', 126.508737, 48.245437, 80);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (84, '231124', '孙吴县', 3, '0456', 127.327315, 49.423941, 80);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (85, '231123', '逊克县', 3, '0456', 128.476152, 49.582974, 80);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (86, '231182', '五大连池市', 3, '0456', 126.197694, 48.512688, 80);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (87, '231000', '牡丹江市', 2, '0453', 129.618602, 44.582962, 2);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (88, '231004', '爱民区', 3, '0453', 129.601232, 44.595443, 87);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (89, '231081', '绥芬河市', 3, '0453', 131.164856, 44.396864, 87);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (90, '231002', '东安区', 3, '0453', 129.623292, 44.582399, 87);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (91, '231084', '宁安市', 3, '0453', 129.470019, 44.346836, 87);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (92, '231086', '东宁市', 3, '0453', 131.125296, 44.063578, 87);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (93, '231003', '阳明区', 3, '0453', 129.634645, 44.596328, 87);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (94, '231025', '林口县', 3, '0453', 130.268402, 45.286645, 87);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (95, '231085', '穆棱市', 3, '0453', 130.527085, 44.91967, 87);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (96, '231005', '西安区', 3, '0453', 129.61311, 44.581032, 87);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (97, '231083', '海林市', 3, '0453', 129.387902, 44.574149, 87);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (98, '230300', '鸡西市', 2, '0467', 130.975966, 45.300046, 2);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (99, '230306', '城子河区', 3, '0467', 131.010501, 45.338248, 98);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (100, '230304', '滴道区', 3, '0467', 130.846823, 45.348812, 98);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (101, '230305', '梨树区', 3, '0467', 130.697781, 45.092195, 98);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (102, '230303', '恒山区', 3, '0467', 130.910636, 45.213242, 98);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (103, '230302', '鸡冠区', 3, '0467', 130.974374, 45.30034, 98);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (104, '230307', '麻山区', 3, '0467', 130.481126, 45.209607, 98);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (105, '230382', '密山市', 3, '0467', 131.874137, 45.54725, 98);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (106, '230321', '鸡东县', 3, '0467', 131.148907, 45.250892, 98);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (107, '230381', '虎林市', 3, '0467', 132.973881, 45.767985, 98);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (108, '230800', '佳木斯市', 2, '0454', 130.361634, 46.809606, 2);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (109, '230881', '同江市', 3, '0454', 132.510119, 47.651131, 108);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (110, '230826', '桦川县', 3, '0454', 130.723713, 47.023039, 108);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (111, '230828', '汤原县', 3, '0454', 129.904463, 46.730048, 108);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (112, '230805', '东风区', 3, '0454', 130.403297, 46.822476, 108);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (113, '230803', '向阳区', 3, '0454', 130.361786, 46.809645, 108);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (114, '230804', '前进区', 3, '0454', 130.377684, 46.812345, 108);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (115, '230811', '郊区', 3, '0454', 130.351588, 46.80712, 108);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (116, '230822', '桦南县', 3, '0454', 130.570112, 46.240118, 108);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (117, '230883', '抚远市', 3, '0454', 134.294501, 48.364707, 108);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (118, '230882', '富锦市', 3, '0454', 132.037951, 47.250747, 108);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (119, '230600', '大庆市', 2, '0459', 125.11272, 46.590734, 2);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (120, '230604', '让胡路区', 3, '0459', 124.868341, 46.653254, 119);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (121, '230605', '红岗区', 3, '0459', 124.889528, 46.403049, 119);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (122, '230622', '肇源县', 3, '0459', 125.081974, 45.518832, 119);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (123, '230606', '大同区', 3, '0459', 124.818509, 46.034304, 119);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (124, '230623', '林甸县', 3, '0459', 124.877742, 47.186411, 119);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (125, '230624', '杜尔伯特蒙古族自治县', 3, '0459', 124.446259, 46.865973, 119);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (126, '230603', '龙凤区', 3, '0459', 125.145794, 46.573948, 119);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (127, '230602', '萨尔图区', 3, '0459', 125.114643, 46.596356, 119);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (128, '230621', '肇州县', 3, '0459', 125.273254, 45.708685, 119);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (129, '230500', '双鸭山市', 2, '0469', 131.157304, 46.643442, 2);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (130, '230502', '尖山区', 3, '0469', 131.15896, 46.642961, 129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (131, '230522', '友谊县', 3, '0469', 131.810622, 46.775159, 129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (132, '230506', '宝山区', 3, '0469', 131.404294, 46.573366, 129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (133, '230505', '四方台区', 3, '0469', 131.333181, 46.594347, 129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (134, '230503', '岭东区', 3, '0469', 131.163675, 46.591076, 129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (135, '230523', '宝清县', 3, '0469', 132.206415, 46.328781, 129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (136, '230524', '饶河县', 3, '0469', 134.021162, 46.801288, 129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (137, '230521', '集贤县', 3, '0469', 131.13933, 46.72898, 129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (138, '440000', '广东省', 1, '', 113.280637, 23.125178, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (139, '440500', '汕头市', 2, '0754', 116.708463, 23.37102, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (140, '440515', '澄海区', 3, '0754', 116.76336, 23.46844, 139);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (141, '440514', '潮南区', 3, '0754', 116.423607, 23.249798, 139);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (142, '440523', '南澳县', 3, '0754', 117.027105, 23.419562, 139);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (143, '440512', '濠江区', 3, '0754', 116.729528, 23.279345, 139);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (144, '440513', '潮阳区', 3, '0754', 116.602602, 23.262336, 139);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (145, '440511', '金平区', 3, '0754', 116.703583, 23.367071, 139);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (146, '440507', '龙湖区', 3, '0754', 116.732015, 23.373754, 139);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (147, '440600', '佛山市', 2, '0757', 113.122717, 23.028762, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (148, '440607', '三水区', 3, '0757', 112.899414, 23.16504, 147);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (149, '440608', '高明区', 3, '0757', 112.882123, 22.893855, 147);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (150, '440606', '顺德区', 3, '0757', 113.281826, 22.75851, 147);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (151, '440605', '南海区', 3, '0757', 113.145577, 23.031562, 147);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (152, '440604', '禅城区', 3, '0757', 113.112414, 23.019643, 147);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (153, '441200', '肇庆市', 2, '0758', 112.472529, 23.051546, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (154, '441224', '怀集县', 3, '0758', 112.182466, 23.913072, 153);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (155, '441225', '封开县', 3, '0758', 111.502973, 23.434731, 153);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (156, '441223', '广宁县', 3, '0758', 112.440419, 23.631486, 153);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (157, '441284', '四会市', 3, '0758', 112.695028, 23.340324, 153);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (158, '441226', '德庆县', 3, '0758', 111.78156, 23.141711, 153);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (159, '441203', '鼎湖区', 3, '0758', 112.565249, 23.155822, 153);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (160, '441202', '端州区', 3, '0758', 112.472329, 23.052662, 153);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (161, '441204', '高要区', 3, '0758', 112.460846, 23.027694, 153);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (162, '441300', '惠州市', 2, '0752', 114.412599, 23.079404, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (163, '441324', '龙门县', 3, '0752', 114.259986, 23.723894, 162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (164, '441323', '惠东县', 3, '0752', 114.723092, 22.983036, 162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (165, '441322', '博罗县', 3, '0752', 114.284254, 23.167575, 162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (166, '441303', '惠阳区', 3, '0752', 114.469444, 22.78851, 162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (167, '441302', '惠城区', 3, '0752', 114.413978, 23.079883, 162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (168, '440300', '深圳市', 2, '0755', 114.085947, 22.547, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (169, '440306', '宝安区', 3, '0755', 113.828671, 22.754741, 168);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (170, '440305', '南山区', 3, '0755', 113.92943, 22.531221, 168);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (171, '440304', '福田区', 3, '0755', 114.05096, 22.541009, 168);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (172, '440308', '盐田区', 3, '0755', 114.235366, 22.555069, 168);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (173, '440303', '罗湖区', 3, '0755', 114.123885, 22.555341, 168);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (174, '440310', '坪山区', 3, '0755', 114.338441, 22.69423, 168);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (175, '440307', '龙岗区', 3, '0755', 114.251372, 22.721511, 168);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (176, '440309', '龙华区', 3, '0755', 114.044346, 22.691963, 168);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (177, '440311', '光明区', 3, '0755', 113.935895, 22.748816, 168);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (178, '440800', '湛江市', 2, '0759', 110.364977, 21.274898, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (179, '440881', '廉江市', 3, '0759', 110.284961, 21.611281, 178);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (180, '440883', '吴川市', 3, '0759', 110.780508, 21.428453, 178);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (181, '440825', '徐闻县', 3, '0759', 110.175718, 20.326083, 178);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (182, '440882', '雷州市', 3, '0759', 110.088275, 20.908523, 178);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (183, '440811', '麻章区', 3, '0759', 110.329167, 21.265997, 178);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (184, '440804', '坡头区', 3, '0759', 110.455632, 21.24441, 178);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (185, '440803', '霞山区', 3, '0759', 110.406382, 21.194229, 178);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (186, '440823', '遂溪县', 3, '0759', 110.255321, 21.376915, 178);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (187, '440802', '赤坎区', 3, '0759', 110.361634, 21.273365, 178);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (188, '440400', '珠海市', 2, '0756', 113.553986, 22.224979, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (189, '440403', '斗门区', 3, '0756', 113.297739, 22.209117, 188);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (190, '440404', '金湾区', 3, '0756', 113.345071, 22.139122, 188);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (191, '440402', '香洲区', 3, '0756', 113.55027, 22.271249, 188);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (192, '445100', '潮州市', 2, '0768', 116.632301, 23.661701, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (193, '445122', '饶平县', 3, '0768', 117.00205, 23.668171, 192);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (194, '445102', '湘桥区', 3, '0768', 116.63365, 23.664675, 192);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (195, '445103', '潮安区', 3, '0768', 116.67931, 23.461012, 192);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (196, '440900', '茂名市', 2, '0668', 110.919229, 21.659751, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (197, '440983', '信宜市', 3, '0668', 110.941656, 22.352681, 196);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (198, '440981', '高州市', 3, '0668', 110.853251, 21.915153, 196);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (199, '440982', '化州市', 3, '0668', 110.63839, 21.654953, 196);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (200, '440904', '电白区', 3, '0668', 111.007264, 21.507219, 196);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (201, '440902', '茂南区', 3, '0668', 110.920542, 21.660425, 196);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (202, '440700', '江门市', 2, '0750', 113.094942, 22.590431, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (203, '440784', '鹤山市', 3, '0750', 112.961795, 22.768104, 202);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (204, '440704', '江海区', 3, '0750', 113.120601, 22.572211, 202);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (205, '440783', '开平市', 3, '0750', 112.692262, 22.366286, 202);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (206, '440781', '台山市', 3, '0750', 112.793414, 22.250713, 202);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (207, '440785', '恩平市', 3, '0750', 112.314051, 22.182956, 202);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (208, '440705', '新会区', 3, '0750', 113.038584, 22.520247, 202);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (209, '440703', '蓬江区', 3, '0750', 113.07859, 22.59677, 202);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (210, '441600', '河源市', 2, '0762', 114.697802, 23.746266, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (211, '441624', '和平县', 3, '0762', 114.941473, 24.44318, 210);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (212, '441622', '龙川县', 3, '0762', 115.256415, 24.101174, 210);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (213, '441623', '连平县', 3, '0762', 114.495952, 24.364227, 210);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (214, '441625', '东源县', 3, '0762', 114.742711, 23.789093, 210);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (215, '441602', '源城区', 3, '0762', 114.696828, 23.746255, 210);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (216, '441621', '紫金县', 3, '0762', 115.184383, 23.633744, 210);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (217, '445300', '云浮市', 2, '0766', 112.044439, 22.929801, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (218, '445322', '郁南县', 3, '0766', 111.535921, 23.237709, 217);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (219, '445381', '罗定市', 3, '0766', 111.578201, 22.765415, 217);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (220, '445321', '新兴县', 3, '0766', 112.23083, 22.703204, 217);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (221, '445302', '云城区', 3, '0766', 112.04471, 22.930827, 217);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (222, '445303', '云安区', 3, '0766', 112.005609, 23.073152, 217);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (223, '441500', '汕尾市', 2, '0660', 115.364238, 22.774485, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (224, '441521', '海丰县', 3, '0660', 115.337324, 22.971042, 223);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (225, '441523', '陆河县', 3, '0660', 115.657565, 23.302682, 223);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (226, '441581', '陆丰市', 3, '0660', 115.644203, 22.946104, 223);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (227, '441502', '城区', 3, '0660', 115.363667, 22.776227, 223);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (228, '441700', '阳江市', 2, '0662', 111.975107, 21.859222, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (229, '441781', '阳春市', 3, '0662', 111.7905, 22.169598, 228);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (230, '441702', '江城区', 3, '0662', 111.968909, 21.859182, 228);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (231, '441721', '阳西县', 3, '0662', 111.617556, 21.75367, 228);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (232, '441704', '阳东区', 3, '0662', 112.011267, 21.864728, 228);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (233, '445200', '揭阳市', 2, '0663', 116.355733, 23.543778, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (234, '445281', '普宁市', 3, '0663', 116.165082, 23.29788, 233);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (235, '445222', '揭西县', 3, '0663', 115.838708, 23.4273, 233);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (236, '445224', '惠来县', 3, '0663', 116.295832, 23.029834, 233);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (237, '445203', '揭东区', 3, '0663', 116.412947, 23.569887, 233);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (238, '445202', '榕城区', 3, '0663', 116.357045, 23.535524, 233);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (239, '441400', '梅州市', 2, '0753', 116.117582, 24.299112, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (240, '441427', '蕉岭县', 3, '0753', 116.170531, 24.653313, 239);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (241, '441426', '平远县', 3, '0753', 115.891729, 24.569651, 239);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (242, '441481', '兴宁市', 3, '0753', 115.731648, 24.138077, 239);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (243, '441424', '五华县', 3, '0753', 115.775004, 23.925424, 239);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (244, '441423', '丰顺县', 3, '0753', 116.184419, 23.752771, 239);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (245, '441422', '大埔县', 3, '0753', 116.69552, 24.351587, 239);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (246, '441403', '梅县区', 3, '0753', 116.083482, 24.267825, 239);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (247, '441402', '梅江区', 3, '0753', 116.12116, 24.302593, 239);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (248, '440100', '广州市', 2, '020', 113.280637, 23.125178, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (249, '440117', '从化区', 3, '020', 113.587386, 23.545283, 248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (250, '440115', '南沙区', 3, '020', 113.53738, 22.794531, 248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (251, '440114', '花都区', 3, '020', 113.211184, 23.39205, 248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (252, '440111', '白云区', 3, '020', 113.262831, 23.162281, 248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (253, '440113', '番禺区', 3, '020', 113.364619, 22.938582, 248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (254, '440103', '荔湾区', 3, '020', 113.243038, 23.124943, 248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (255, '440105', '海珠区', 3, '020', 113.262008, 23.103131, 248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (256, '440118', '增城区', 3, '020', 113.829579, 23.290497, 248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (257, '440104', '越秀区', 3, '020', 113.280714, 23.125624, 248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (258, '440112', '黄埔区', 3, '020', 113.450761, 23.103239, 248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (259, '440106', '天河区', 3, '020', 113.335367, 23.13559, 248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (260, '440200', '韶关市', 2, '0751', 113.591544, 24.801322, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (261, '440222', '始兴县', 3, '0751', 114.067205, 24.948364, 260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (262, '440282', '南雄市', 3, '0751', 114.311231, 25.115328, 260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (263, '440204', '浈江区', 3, '0751', 113.599224, 24.803977, 260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (264, '440224', '仁化县', 3, '0751', 113.748627, 25.088226, 260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (265, '440232', '乳源瑶族自治县', 3, '0751', 113.278417, 24.776109, 260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (266, '440229', '翁源县', 3, '0751', 114.131289, 24.353887, 260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (267, '440205', '曲江区', 3, '0751', 113.605582, 24.680195, 260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (268, '440203', '武江区', 3, '0751', 113.588289, 24.80016, 260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (269, '440233', '新丰县', 3, '0751', 114.207034, 24.055412, 260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (270, '440281', '乐昌市', 3, '0751', 113.352413, 25.128445, 260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (271, '441800', '清远市', 2, '0763', 113.051227, 23.685022, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (272, '441882', '连州市', 3, '0763', 112.379271, 24.783966, 271);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (273, '441825', '连山壮族瑶族自治县', 3, '0763', 112.086555, 24.567271, 271);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (274, '441826', '连南瑶族自治县', 3, '0763', 112.290808, 24.719097, 271);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (275, '441881', '英德市', 3, '0763', 113.405404, 24.18612, 271);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (276, '441821', '佛冈县', 3, '0763', 113.534094, 23.866739, 271);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (277, '441823', '阳山县', 3, '0763', 112.634019, 24.470286, 271);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (278, '441803', '清新区', 3, '0763', 113.015203, 23.736949, 271);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (279, '441802', '清城区', 3, '0763', 113.048698, 23.688976, 271);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (280, '442000', '中山市', 2, '0760', 113.382391, 22.521113, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (281, '441900', '东莞市', 2, '0769', 113.746262, 23.046237, 138);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (282, '410000', '河南省', 1, '', 113.665412, 34.757975, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (283, '410300', '洛阳市', 2, '0379', 112.434468, 34.663041, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (284, '410323', '新安县', 3, '0379', 112.141403, 34.728679, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (285, '410324', '栾川县', 3, '0379', 111.618386, 33.783195, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (286, '410329', '伊川县', 3, '0379', 112.429384, 34.423416, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (287, '410306', '吉利区', 3, '0379', 112.584796, 34.899093, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (288, '410328', '洛宁县', 3, '0379', 111.655399, 34.387179, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (289, '410381', '偃师市', 3, '0379', 112.787739, 34.723042, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (290, '410304', '瀍河回族区', 3, '0379', 112.491625, 34.684738, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (291, '410302', '老城区', 3, '0379', 112.477298, 34.682945, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (292, '410303', '西工区', 3, '0379', 112.443232, 34.667847, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (293, '410322', '孟津县', 3, '0379', 112.443892, 34.826485, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (294, '410311', '洛龙区', 3, '0379', 112.456634, 34.618557, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (295, '410305', '涧西区', 3, '0379', 112.399243, 34.654251, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (296, '410327', '宜阳县', 3, '0379', 112.179989, 34.516478, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (297, '410326', '汝阳县', 3, '0379', 112.473789, 34.15323, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (298, '410325', '嵩县', 3, '0379', 112.087765, 34.131563, 283);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (299, '411200', '三门峡市', 2, '0398', 111.194099, 34.777338, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (300, '411202', '湖滨区', 3, '0398', 111.19487, 34.77812, 299);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (301, '411224', '卢氏县', 3, '0398', 111.052649, 34.053995, 299);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (302, '411221', '渑池县', 3, '0398', 111.762992, 34.763487, 299);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (303, '411282', '灵宝市', 3, '0398', 110.88577, 34.521264, 299);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (304, '411203', '陕州区', 3, '0398', 111.103851, 34.720244, 299);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (305, '411281', '义马市', 3, '0398', 111.869417, 34.746868, 299);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (306, '411100', '漯河市', 2, '0395', 114.026405, 33.575855, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (307, '411104', '召陵区', 3, '0395', 114.051686, 33.567555, 306);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (308, '411122', '临颍县', 3, '0395', 113.938891, 33.80609, 306);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (309, '411102', '源汇区', 3, '0395', 114.017948, 33.565441, 306);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (310, '411103', '郾城区', 3, '0395', 114.016813, 33.588897, 306);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (311, '411121', '舞阳县', 3, '0395', 113.610565, 33.436278, 306);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (312, '411000', '许昌市', 2, '0374', 113.826063, 34.022956, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (313, '411003', '建安区', 3, '0374', 113.842898, 34.005018, 312);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (314, '411002', '魏都区', 3, '0374', 113.828307, 34.02711, 312);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (315, '411082', '长葛市', 3, '0374', 113.768912, 34.219257, 312);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (316, '411081', '禹州市', 3, '0374', 113.471316, 34.154403, 312);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (317, '411024', '鄢陵县', 3, '0374', 114.188507, 34.100502, 312);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (318, '411025', '襄城县', 3, '0374', 113.493166, 33.855943, 312);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (319, '411300', '南阳市', 2, '0377', 112.540918, 32.999082, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (320, '411323', '西峡县', 3, '0377', 111.485772, 33.302981, 319);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (321, '411327', '社旗县', 3, '0377', 112.938279, 33.056126, 319);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (322, '411330', '桐柏县', 3, '0377', 113.406059, 32.367153, 319);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (323, '411303', '卧龙区', 3, '0377', 112.528789, 32.989877, 319);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (324, '411321', '南召县', 3, '0377', 112.435583, 33.488617, 319);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (325, '411328', '唐河县', 3, '0377', 112.838492, 32.687892, 319);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (326, '411322', '方城县', 3, '0377', 113.010933, 33.255138, 319);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (327, '411302', '宛城区', 3, '0377', 112.544591, 32.994857, 319);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (328, '411329', '新野县', 3, '0377', 112.365624, 32.524006, 319);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (329, '411324', '镇平县', 3, '0377', 112.232722, 33.036651, 319);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (330, '411326', '淅川县', 3, '0377', 111.489026, 33.136106, 319);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (331, '411381', '邓州市', 3, '0377', 112.092716, 32.681642, 319);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (332, '411325', '内乡县', 3, '0377', 111.843801, 33.046358, 319);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (333, '411500', '信阳市', 2, '0376', 114.075031, 32.123274, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (334, '411526', '潢川县', 3, '0376', 115.050123, 32.134024, 333);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (335, '411527', '淮滨县', 3, '0376', 115.415451, 32.452639, 333);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (336, '411521', '罗山县', 3, '0376', 114.533414, 32.203206, 333);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (337, '411522', '光山县', 3, '0376', 114.903577, 32.010398, 333);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (338, '411523', '新县', 3, '0376', 114.87705, 31.63515, 333);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (339, '411502', '浉河区', 3, '0376', 114.075031, 32.123274, 333);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (340, '411525', '固始县', 3, '0376', 115.667328, 32.183074, 333);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (341, '411524', '商城县', 3, '0376', 115.406297, 31.799982, 333);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (342, '411528', '息县', 3, '0376', 114.740713, 32.344744, 333);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (343, '411503', '平桥区', 3, '0376', 114.126027, 32.098395, 333);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (344, '419001', '济源市', 2, '1391', 112.590047, 35.090378, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (345, '411700', '驻马店市', 2, '0396', 114.024736, 32.980169, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (346, '411722', '上蔡县', 3, '0396', 114.266892, 33.264719, 345);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (347, '411721', '西平县', 3, '0396', 114.026864, 33.382315, 345);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (348, '411729', '新蔡县', 3, '0396', 114.975246, 32.749948, 345);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (349, '411726', '泌阳县', 3, '0396', 113.32605, 32.725129, 345);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (350, '411702', '驿城区', 3, '0396', 114.029149, 32.977559, 345);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (351, '411725', '确山县', 3, '0396', 114.026679, 32.801538, 345);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (352, '411724', '正阳县', 3, '0396', 114.38948, 32.601826, 345);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (353, '411728', '遂平县', 3, '0396', 114.00371, 33.14698, 345);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (354, '411727', '汝南县', 3, '0396', 114.359495, 33.004535, 345);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (355, '411723', '平舆县', 3, '0396', 114.637105, 32.955626, 345);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (356, '410900', '濮阳市', 2, '0393', 115.041299, 35.768234, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (357, '410927', '台前县', 3, '0393', 115.855681, 35.996474, 356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (358, '410923', '南乐县', 3, '0393', 115.204336, 36.075204, 356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (359, '410926', '范县', 3, '0393', 115.504212, 35.851977, 356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (360, '410902', '华龙区', 3, '0393', 115.03184, 35.760473, 356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (361, '410922', '清丰县', 3, '0393', 115.107287, 35.902413, 356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (362, '410928', '濮阳县', 3, '0393', 115.023844, 35.710349, 356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (363, '410800', '焦作市', 2, '0391', 113.238266, 35.23904, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (364, '410883', '孟州市', 3, '0391', 112.78708, 34.90963, 363);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (365, '410825', '温县', 3, '0391', 113.079118, 34.941233, 363);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (366, '410882', '沁阳市', 3, '0391', 112.934538, 35.08901, 363);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (367, '410804', '马村区', 3, '0391', 113.321703, 35.265453, 363);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (368, '410823', '武陟县', 3, '0391', 113.408334, 35.09885, 363);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (369, '410822', '博爱县', 3, '0391', 113.069313, 35.170351, 363);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (370, '410821', '修武县', 3, '0391', 113.447465, 35.229923, 363);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (371, '410802', '解放区', 3, '0391', 113.226126, 35.241353, 363);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (372, '410811', '山阳区', 3, '0391', 113.26766, 35.21476, 363);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (373, '410803', '中站区', 3, '0391', 113.175485, 35.236145, 363);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (374, '410100', '郑州市', 2, '0371', 113.665412, 34.757975, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (375, '410185', '登封市', 3, '0371', 113.037768, 34.459939, 374);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (376, '410102', '中原区', 3, '0371', 113.611576, 34.748286, 374);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (377, '410183', '新密市', 3, '0371', 113.380616, 34.537846, 374);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (378, '410184', '新郑市', 3, '0371', 113.73967, 34.394219, 374);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (379, '410181', '巩义市', 3, '0371', 112.98283, 34.75218, 374);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (380, '410182', '荥阳市', 3, '0371', 113.391523, 34.789077, 374);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (381, '410106', '上街区', 3, '0371', 113.298282, 34.808689, 374);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (382, '410103', '二七区', 3, '0371', 113.645422, 34.730936, 374);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (383, '410105', '金水区', 3, '0371', 113.686037, 34.775838, 374);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (384, '410108', '惠济区', 3, '0371', 113.61836, 34.828591, 374);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (385, '410122', '中牟县', 3, '0371', 114.022521, 34.721976, 374);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (386, '410104', '管城回族区', 3, '0371', 113.685313, 34.746453, 374);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (387, '410500', '安阳市', 2, '0372', 114.352482, 36.103442, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (388, '410523', '汤阴县', 3, '0372', 114.362357, 35.922349, 387);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (389, '410505', '殷都区', 3, '0372', 114.300098, 36.108974, 387);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (390, '410581', '林州市', 3, '0372', 113.823767, 36.063403, 387);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (391, '410506', '龙安区', 3, '0372', 114.323522, 36.095568, 387);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (392, '410527', '内黄县', 3, '0372', 114.904582, 35.953702, 387);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (393, '410503', '北关区', 3, '0372', 114.352646, 36.10978, 387);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (394, '410522', '安阳县', 3, '0372', 114.130207, 36.130585, 387);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (395, '410502', '文峰区', 3, '0372', 114.352562, 36.098101, 387);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (396, '410526', '滑县', 3, '0372', 114.524, 35.574628, 387);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (397, '410700', '新乡市', 2, '0373', 113.883991, 35.302616, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (398, '410782', '辉县市', 3, '0373', 113.802518, 35.461318, 397);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (399, '410704', '凤泉区', 3, '0373', 113.906712, 35.379855, 397);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (400, '410783', '长垣市', 3, '0373', 114.673807, 35.19615, 397);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (401, '410711', '牧野区', 3, '0373', 113.89716, 35.312974, 397);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (402, '410703', '卫滨区', 3, '0373', 113.866065, 35.304905, 397);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (403, '410724', '获嘉县', 3, '0373', 113.657249, 35.261685, 397);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (404, '410721', '新乡县', 3, '0373', 113.806186, 35.190021, 397);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (405, '410727', '封丘县', 3, '0373', 114.423405, 35.04057, 397);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (406, '410725', '原阳县', 3, '0373', 113.965966, 35.054001, 397);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (407, '410726', '延津县', 3, '0373', 114.200982, 35.149515, 397);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (408, '410781', '卫辉市', 3, '0373', 114.065855, 35.404295, 397);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (409, '410702', '红旗区', 3, '0373', 113.878158, 35.302684, 397);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (410, '410600', '鹤壁市', 2, '0392', 114.295444, 35.748236, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (411, '410602', '鹤山区', 3, '0392', 114.166551, 35.936128, 410);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (412, '410603', '山城区', 3, '0392', 114.184202, 35.896058, 410);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (413, '410622', '淇县', 3, '0392', 114.200379, 35.609478, 410);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (414, '410611', '淇滨区', 3, '0392', 114.293917, 35.748382, 410);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (415, '410621', '浚县', 3, '0392', 114.550162, 35.671282, 410);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (416, '410200', '开封市', 2, '0378', 114.341447, 34.797049, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (417, '410203', '顺河回族区', 3, '0378', 114.364875, 34.800459, 416);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (418, '410212', '祥符区', 3, '0378', 114.437622, 34.756476, 416);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (419, '410205', '禹王台区', 3, '0378', 114.350246, 34.779727, 416);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (420, '410204', '鼓楼区', 3, '0378', 114.3485, 34.792383, 416);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (421, '410202', '龙亭区', 3, '0378', 114.353348, 34.799833, 416);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (422, '410223', '尉氏县', 3, '0378', 114.193927, 34.412256, 416);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (423, '410225', '兰考县', 3, '0378', 114.820572, 34.829899, 416);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (424, '410221', '杞县', 3, '0378', 114.770472, 34.554585, 416);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (425, '410222', '通许县', 3, '0378', 114.467734, 34.477302, 416);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (426, '411600', '周口市', 2, '0394', 114.649653, 33.620357, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (427, '411624', '沈丘县', 3, '0394', 115.078375, 33.395514, 426);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (428, '411681', '项城市', 3, '0394', 114.899521, 33.443085, 426);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (429, '411602', '川汇区', 3, '0394', 114.652136, 33.614836, 426);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (430, '411623', '商水县', 3, '0394', 114.60927, 33.543845, 426);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (431, '411628', '鹿邑县', 3, '0394', 115.486386, 33.861067, 426);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (432, '411625', '郸城县', 3, '0394', 115.189, 33.643852, 426);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (433, '411603', '淮阳区', 3, '0394', 114.870166, 33.732547, 426);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (434, '411622', '西华县', 3, '0394', 114.530067, 33.784378, 426);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (435, '411621', '扶沟县', 3, '0394', 114.392008, 34.054061, 426);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (436, '411627', '太康县', 3, '0394', 114.853834, 34.065312, 426);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (437, '410400', '平顶山市', 2, '0375', 113.307718, 33.735241, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (438, '410404', '石龙区', 3, '0375', 112.889885, 33.901538, 437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (439, '410403', '卫东区', 3, '0375', 113.310327, 33.739285, 437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (440, '410423', '鲁山县', 3, '0375', 112.906703, 33.740325, 437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (441, '410411', '湛河区', 3, '0375', 113.320873, 33.725681, 437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (442, '410481', '舞钢市', 3, '0375', 113.52625, 33.302082, 437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (443, '410425', '郏县', 3, '0375', 113.220451, 33.971993, 437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (444, '410422', '叶县', 3, '0375', 113.358298, 33.621252, 437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (445, '410421', '宝丰县', 3, '0375', 113.066812, 33.866359, 437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (446, '410482', '汝州市', 3, '0375', 112.845336, 34.167408, 437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (447, '410402', '新华区', 3, '0375', 113.299061, 33.737579, 437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (448, '411400', '商丘市', 2, '0370', 115.650497, 34.437054, 282);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (449, '411423', '宁陵县', 3, '0370', 115.320055, 34.449299, 448);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (450, '411402', '梁园区', 3, '0370', 115.65459, 34.436553, 448);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (451, '411481', '永城市', 3, '0370', 116.449672, 33.931318, 448);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (452, '411403', '睢阳区', 3, '0370', 115.653813, 34.390536, 448);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (453, '411422', '睢县', 3, '0370', 115.070109, 34.428433, 448);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (454, '411426', '夏邑县', 3, '0370', 116.13989, 34.240894, 448);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (455, '411425', '虞城县', 3, '0370', 115.863811, 34.399634, 448);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (456, '411421', '民权县', 3, '0370', 115.148146, 34.648455, 448);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (457, '411424', '柘城县', 3, '0370', 115.307433, 34.075277, 448);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (458, '150000', '内蒙古自治区', 1, '', 111.670801, 40.818311, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (459, '150600', '鄂尔多斯市', 2, '0477', 109.99029, 39.817179, 458);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (460, '150621', '达拉特旗', 3, '0477', 110.040281, 40.404076, 459);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (461, '150624', '鄂托克旗', 3, '0477', 107.982604, 39.095752, 459);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (462, '150622', '准格尔旗', 3, '0477', 111.238332, 39.865221, 459);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (463, '150625', '杭锦旗', 3, '0477', 108.736324, 39.831789, 459);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (464, '150626', '乌审旗', 3, '0477', 108.842454, 38.596611, 459);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (465, '150623', '鄂托克前旗', 3, '0477', 107.48172, 38.183257, 459);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (466, '150627', '伊金霍洛旗', 3, '0477', 109.787402, 39.604312, 459);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (467, '150603', '康巴什区', 3, '0477', 109.790076, 39.607472, 459);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (468, '150602', '东胜区', 3, '0477', 109.98945, 39.81788, 459);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (469, '150200', '包头市', 2, '0472', 109.840405, 40.658168, 458);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (470, '150221', '土默特右旗', 3, '0472', 110.526766, 40.566434, 469);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (471, '150206', '白云鄂博矿区', 3, '0472', 109.97016, 41.769246, 469);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (472, '150223', '达尔罕茂明安联合旗', 3, '0472', 110.438452, 41.702836, 469);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (473, '150207', '九原区', 3, '0472', 109.968122, 40.600581, 469);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (474, '150205', '石拐区', 3, '0472', 110.272565, 40.672094, 469);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (475, '150204', '青山区', 3, '0472', 109.880049, 40.668558, 469);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (476, '150202', '东河区', 3, '0472', 110.026895, 40.587056, 469);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (477, '150203', '昆都仑区', 3, '0472', 109.822932, 40.661345, 469);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (478, '150222', '固阳县', 3, '0472', 110.063421, 41.030004, 469);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (479, '150800', '巴彦淖尔市', 2, '0478', 107.416959, 40.757402, 458);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (480, '150824', '乌拉特中旗', 3, '0478', 108.515255, 41.57254, 479);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (481, '150821', '五原县', 3, '0478', 108.270658, 41.097639, 479);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (482, '150802', '临河区', 3, '0478', 107.417018, 40.757092, 479);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (483, '150822', '磴口县', 3, '0478', 107.006056, 40.330479, 479);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (484, '150823', '乌拉特前旗', 3, '0478', 108.656816, 40.725209, 479);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (485, '150826', '杭锦后旗', 3, '0478', 107.147682, 40.888797, 479);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (486, '150825', '乌拉特后旗', 3, '0478', 107.074941, 41.084307, 479);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (487, '150300', '乌海市', 2, '0473', 106.825563, 39.673734, 458);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (488, '150304', '乌达区', 3, '0473', 106.722711, 39.502288, 487);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (489, '150303', '海南区', 3, '0473', 106.884789, 39.44153, 487);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (490, '150302', '海勃湾区', 3, '0473', 106.817762, 39.673527, 487);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (491, '150700', '呼伦贝尔市', 2, '0470', 119.758168, 49.215333, 458);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (492, '150784', '额尔古纳市', 3, '0470', 120.178636, 50.2439, 491);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (493, '150782', '牙克石市', 3, '0470', 120.729005, 49.287024, 491);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (494, '150785', '根河市', 3, '0470', 121.532724, 50.780454, 491);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (495, '150721', '阿荣旗', 3, '0470', 123.464615, 48.130503, 491);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (496, '150702', '海拉尔区', 3, '0470', 119.764923, 49.213889, 491);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (497, '150725', '陈巴尔虎旗', 3, '0470', 119.437609, 49.328422, 491);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (498, '150783', '扎兰屯市', 3, '0470', 122.744401, 48.007412, 491);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (499, '150722', '莫力达瓦达斡尔族自治旗', 3, '0470', 124.507401, 48.478385, 491);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (500, '150724', '鄂温克族自治旗', 3, '0470', 119.754041, 49.143293, 491);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (501, '150727', '新巴尔虎右旗', 3, '0470', 116.825991, 48.669134, 491);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (502, '150703', '扎赉诺尔区', 3, '0470', 117.716373, 49.456567, 491);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (503, '150781', '满洲里市', 3, '0470', 117.455561, 49.590788, 491);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (504, '150723', '鄂伦春自治旗', 3, '0470', 123.725684, 50.590177, 491);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (505, '150726', '新巴尔虎左旗', 3, '0470', 118.267454, 48.216571, 491);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (506, '152900', '阿拉善盟', 2, '0483', 105.706422, 38.844814, 458);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (507, '152923', '额济纳旗', 3, '0483', 101.06944, 41.958813, 506);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (508, '152922', '阿拉善右旗', 3, '0483', 101.671984, 39.21159, 506);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (509, '152921', '阿拉善左旗', 3, '0483', 105.70192, 38.847241, 506);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (510, '150500', '通辽市', 2, '0475', 122.263119, 43.617429, 458);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (511, '150581', '霍林郭勒市', 3, '0475', 119.657862, 45.532361, 510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (512, '150526', '扎鲁特旗', 3, '0475', 120.905275, 44.555294, 510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (513, '150521', '科尔沁左翼中旗', 3, '0475', 123.313873, 44.127166, 510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (514, '150525', '奈曼旗', 3, '0475', 120.662543, 42.84685, 510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (515, '150523', '开鲁县', 3, '0475', 121.308797, 43.602432, 510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (516, '150502', '科尔沁区', 3, '0475', 122.264042, 43.617422, 510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (517, '150524', '库伦旗', 3, '0475', 121.774886, 42.734692, 510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (518, '150522', '科尔沁左翼后旗', 3, '0475', 122.355155, 42.954564, 510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (519, '152200', '兴安盟', 2, '0482', 122.070317, 46.076268, 458);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (520, '152202', '阿尔山市', 3, '0482', 119.943656, 47.177, 519);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (521, '152222', '科尔沁右翼中旗', 3, '0482', 121.472818, 45.059645, 519);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (522, '152224', '突泉县', 3, '0482', 121.564856, 45.380986, 519);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (523, '152201', '乌兰浩特市', 3, '0482', 122.068975, 46.077238, 519);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (524, '152223', '扎赉特旗', 3, '0482', 122.909332, 46.725136, 519);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (525, '152221', '科尔沁右翼前旗', 3, '0482', 121.957544, 46.076497, 519);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (526, '152500', '锡林郭勒盟', 2, '0479', 116.090996, 43.944018, 458);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (527, '152526', '西乌珠穆沁旗', 3, '0479', 117.615249, 44.586147, 526);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (528, '152502', '锡林浩特市', 3, '0479', 116.091903, 43.944301, 526);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (529, '152525', '东乌珠穆沁旗', 3, '0479', 116.980022, 45.510307, 526);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (530, '152522', '阿巴嘎旗', 3, '0479', 114.970618, 44.022728, 526);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (531, '152523', '苏尼特左旗', 3, '0479', 113.653412, 43.854108, 526);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (532, '152524', '苏尼特右旗', 3, '0479', 112.65539, 42.746662, 526);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (533, '152501', '二连浩特市', 3, '0479', 111.97981, 43.652895, 526);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (534, '152528', '镶黄旗', 3, '0479', 113.843869, 42.239229, 526);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (535, '152530', '正蓝旗', 3, '0479', 116.003311, 42.245895, 526);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (536, '152529', '正镶白旗', 3, '0479', 115.031423, 42.286807, 526);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (537, '152531', '多伦县', 3, '0479', 116.477288, 42.197962, 526);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (538, '152527', '太仆寺旗', 3, '0479', 115.28728, 41.895199, 526);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (539, '150100', '呼和浩特市', 2, '0471', 111.670801, 40.818311, 458);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (540, '150123', '和林格尔县', 3, '0471', 111.824143, 40.380288, 539);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (541, '150103', '回民区', 3, '0471', 111.662162, 40.815149, 539);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (542, '150104', '玉泉区', 3, '0471', 111.66543, 40.799421, 539);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (543, '150125', '武川县', 3, '0471', 111.456563, 41.094483, 539);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (544, '150121', '土默特左旗', 3, '0471', 111.133615, 40.720416, 539);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (545, '150122', '托克托县', 3, '0471', 111.197317, 40.276729, 539);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (546, '150105', '赛罕区', 3, '0471', 111.698463, 40.807834, 539);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (547, '150102', '新城区', 3, '0471', 111.685964, 40.826225, 539);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (548, '150124', '清水河县', 3, '0471', 111.67222, 39.912479, 539);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (549, '150400', '赤峰市', 2, '0476', 118.956806, 42.275317, 458);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (550, '150421', '阿鲁科尔沁旗', 3, '0476', 120.094969, 43.87877, 549);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (551, '150424', '林西县', 3, '0476', 118.05775, 43.605326, 549);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (552, '150422', '巴林左旗', 3, '0476', 119.391737, 43.980715, 549);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (553, '150423', '巴林右旗', 3, '0476', 118.678347, 43.528963, 549);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (554, '150425', '克什克腾旗', 3, '0476', 117.542465, 43.256233, 549);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (555, '150426', '翁牛特旗', 3, '0476', 119.022619, 42.937128, 549);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (556, '150404', '松山区', 3, '0476', 118.938958, 42.281046, 549);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (557, '150402', '红山区', 3, '0476', 118.961087, 42.269732, 549);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (558, '150428', '喀喇沁旗', 3, '0476', 118.708572, 41.92778, 549);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (559, '150403', '元宝山区', 3, '0476', 119.289877, 42.041168, 549);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (560, '150430', '敖汉旗', 3, '0476', 119.906486, 42.287012, 549);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (561, '150429', '宁城县', 3, '0476', 119.339242, 41.598692, 549);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (562, '150900', '乌兰察布市', 2, '0474', 113.114543, 41.034126, 458);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (563, '150929', '四子王旗', 3, '0474', 111.70123, 41.528114, 562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (564, '150927', '察哈尔右翼中旗', 3, '0474', 112.633563, 41.274212, 562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (565, '150923', '商都县', 3, '0474', 113.560643, 41.560163, 562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (566, '150924', '兴和县', 3, '0474', 113.834009, 40.872437, 562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (567, '150922', '化德县', 3, '0474', 114.01008, 41.899335, 562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (568, '150928', '察哈尔右翼后旗', 3, '0474', 113.1906, 41.447213, 562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (569, '150921', '卓资县', 3, '0474', 112.577702, 40.89576, 562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (570, '150981', '丰镇市', 3, '0474', 113.163462, 40.437534, 562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (571, '150926', '察哈尔右翼前旗', 3, '0474', 113.211958, 40.786859, 562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (572, '150902', '集宁区', 3, '0474', 113.116453, 41.034134, 562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (573, '150925', '凉城县', 3, '0474', 112.500911, 40.531627, 562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (574, '650000', '新疆维吾尔自治区', 1, '', 87.617733, 43.792818, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (575, '659007', '双河市', 2, '1909', 82.353656, 44.840524, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (576, '659005', '北屯市', 2, '1906', 87.824932, 47.353177, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (577, '652700', '博尔塔拉蒙古自治州', 2, '0909', 82.074778, 44.903258, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (578, '652723', '温泉县', 3, '0909', 81.03099, 44.973751, 577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (579, '652702', '阿拉山口市', 3, '0909', 82.569389, 45.16777, 577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (580, '652701', '博乐市', 3, '0909', 82.072237, 44.903087, 577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (581, '652722', '精河县', 3, '0909', 82.892938, 44.605645, 577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (582, '659006', '铁门关市', 2, '1996', 85.501218, 41.827251, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (583, '659008', '可克达拉市', 2, '1999', 80.63579, 43.6832, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (584, '650100', '乌鲁木齐市', 2, '0991', 87.617733, 43.792818, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (585, '650107', '达坂城区', 3, '0991', 88.30994, 43.36181, 584);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (586, '650102', '天山区', 3, '0991', 87.620116, 43.796428, 584);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (587, '650105', '水磨沟区', 3, '0991', 87.613093, 43.816747, 584);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (588, '650109', '米东区', 3, '0991', 87.691801, 43.960982, 584);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (589, '650106', '头屯河区', 3, '0991', 87.425823, 43.876053, 584);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (590, '650121', '乌鲁木齐县', 3, '0991', 87.505603, 43.982546, 584);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (591, '650103', '沙依巴克区', 3, '0991', 87.596639, 43.788872, 584);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (592, '650104', '新市区', 3, '0991', 87.560653, 43.870882, 584);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (593, '659009', '昆玉市', 2, '1903', 79.287372, 37.207994, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (594, '652800', '巴音郭楞蒙古自治州', 2, '0996', 86.150969, 41.768552, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (595, '652828', '和硕县', 3, '0996', 86.864947, 42.268863, 594);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (596, '652827', '和静县', 3, '0996', 86.391067, 42.31716, 594);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (597, '652824', '若羌县', 3, '0996', 88.168807, 39.023807, 594);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (598, '652826', '焉耆回族自治县', 3, '0996', 86.5698, 42.064349, 594);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (599, '652829', '博湖县', 3, '0996', 86.631576, 41.980166, 594);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (600, '652825', '且末县', 3, '0996', 85.532629, 38.138562, 594);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (601, '652801', '库尔勒市', 3, '0996', 86.145948, 41.763122, 594);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (602, '652822', '轮台县', 3, '0996', 84.248542, 41.781266, 594);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (603, '652823', '尉犁县', 3, '0996', 86.263412, 41.337428, 594);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (604, '653200', '和田地区', 2, '0903', 79.92533, 37.110687, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (605, '653227', '民丰县', 3, '0903', 82.692354, 37.064909, 604);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (606, '653226', '于田县', 3, '0903', 81.667845, 36.854628, 604);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (607, '653201', '和田市', 3, '0903', 79.927542, 37.108944, 604);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (608, '653221', '和田县', 3, '0903', 79.81907, 37.120031, 604);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (609, '653225', '策勒县', 3, '0903', 80.803572, 37.001672, 604);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (610, '653223', '皮山县', 3, '0903', 78.282301, 37.616332, 604);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (611, '653224', '洛浦县', 3, '0903', 80.184038, 37.074377, 604);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (612, '653222', '墨玉县', 3, '0903', 79.736629, 37.271511, 604);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (613, '654200', '塔城地区', 2, '0901', 82.985732, 46.746301, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (614, '654201', '塔城市', 3, '0901', 82.983988, 46.746281, 613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (615, '654221', '额敏县', 3, '0901', 83.622118, 46.522555, 613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (616, '654226', '和布克赛尔蒙古自治县', 3, '0901', 85.733551, 46.793001, 613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (617, '654225', '裕民县', 3, '0901', 82.982157, 46.202781, 613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (618, '654224', '托里县', 3, '0901', 83.60469, 45.935863, 613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (619, '654202', '乌苏市', 3, '0901', 84.677624, 44.430115, 613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (620, '654223', '沙湾县', 3, '0901', 85.622508, 44.329544, 613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (621, '654300', '阿勒泰地区', 2, '0906', 88.13963, 47.848393, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (622, '654321', '布尔津县', 3, '0906', 86.86186, 47.70453, 621);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (623, '654324', '哈巴河县', 3, '0906', 86.418964, 48.059284, 621);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (624, '654326', '吉木乃县', 3, '0906', 85.876064, 47.434633, 621);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (625, '654325', '青河县', 3, '0906', 90.381561, 46.672446, 621);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (626, '654322', '富蕴县', 3, '0906', 89.524993, 46.993106, 621);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (627, '654301', '阿勒泰市', 3, '0906', 88.138743, 47.848911, 621);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (628, '654323', '福海县', 3, '0906', 87.494569, 47.113128, 621);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (629, '652300', '昌吉回族自治州', 2, '0994', 87.304012, 44.014577, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (630, '652325', '奇台县', 3, '0994', 89.591437, 44.021996, 629);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (631, '652324', '玛纳斯县', 3, '0994', 86.217687, 44.305625, 629);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (632, '652323', '呼图壁县', 3, '0994', 86.888613, 44.189342, 629);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (633, '652328', '木垒哈萨克自治县', 3, '0994', 90.282833, 43.832442, 629);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (634, '652302', '阜康市', 3, '0994', 87.98384, 44.152153, 629);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (635, '652301', '昌吉市', 3, '0994', 87.304112, 44.013183, 629);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (636, '652327', '吉木萨尔县', 3, '0994', 89.181288, 43.997162, 629);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (637, '659001', '石河子市', 2, '0993', 86.041075, 44.305886, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (638, '659004', '五家渠市', 2, '1994', 87.526884, 44.167401, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (639, '654000', '伊犁哈萨克自治州', 2, '0999', 81.317946, 43.92186, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (640, '654003', '奎屯市', 3, '0999', 84.901602, 44.423445, 639);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (641, '654021', '伊宁县', 3, '0999', 81.524671, 43.977876, 639);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (642, '654025', '新源县', 3, '0999', 83.258493, 43.434249, 639);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (643, '654024', '巩留县', 3, '0999', 82.227044, 43.481618, 639);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (644, '654028', '尼勒克县', 3, '0999', 82.504119, 43.789737, 639);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (645, '654027', '特克斯县', 3, '0999', 81.840058, 43.214861, 639);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (646, '654026', '昭苏县', 3, '0999', 81.126029, 43.157765, 639);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (647, '654022', '察布查尔锡伯自治县', 3, '0999', 81.150874, 43.838883, 639);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (648, '654023', '霍城县', 3, '0999', 80.872508, 44.049912, 639);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (649, '654004', '霍尔果斯市', 3, '0999', 80.420759, 44.201669, 639);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (650, '654002', '伊宁市', 3, '0999', 81.316343, 43.922209, 639);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (651, '659002', '阿拉尔市', 2, '1997', 81.285884, 40.541914, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (652, '650400', '吐鲁番市', 2, '0995', 89.184078, 42.947613, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (653, '650421', '鄯善县', 3, '0995', 90.212692, 42.865503, 652);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (654, '650422', '托克逊县', 3, '0995', 88.655771, 42.793536, 652);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (655, '650402', '高昌区', 3, '0995', 89.182324, 42.947627, 652);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (656, '653100', '喀什地区', 2, '0998', 75.989138, 39.467664, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (657, '653122', '疏勒县', 3, '0998', 76.053653, 39.399461, 656);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (658, '653127', '麦盖提县', 3, '0998', 77.651538, 38.903384, 656);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (659, '653125', '莎车县', 3, '0998', 77.248884, 38.414499, 656);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (660, '653129', '伽师县', 3, '0998', 76.741982, 39.494325, 656);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (661, '653131', '塔什库尔干塔吉克自治县', 3, '0998', 75.228068, 37.775437, 656);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (662, '653126', '叶城县', 3, '0998', 77.420353, 37.884679, 656);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (663, '653124', '泽普县', 3, '0998', 77.273593, 38.191217, 656);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (664, '653130', '巴楚县', 3, '0998', 78.55041, 39.783479, 656);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (665, '653121', '疏附县', 3, '0998', 75.863075, 39.378306, 656);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (666, '653123', '英吉沙县', 3, '0998', 76.174292, 38.929839, 656);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (667, '653101', '喀什市', 3, '0998', 75.98838, 39.467861, 656);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (668, '653128', '岳普湖县', 3, '0998', 76.7724, 39.235248, 656);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (669, '650500', '哈密市', 2, '0902', 93.51316, 42.833248, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (670, '650502', '伊州区', 3, '0902', 93.509174, 42.833888, 669);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (671, '650521', '巴里坤哈萨克自治县', 3, '0902', 93.021795, 43.599032, 669);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (672, '650522', '伊吾县', 3, '0902', 94.692773, 43.252012, 669);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (673, '659003', '图木舒克市', 2, '1998', 79.077978, 39.867316, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (674, '653000', '克孜勒苏柯尔克孜自治州', 2, '0908', 76.172825, 39.713431, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (675, '653024', '乌恰县', 3, '0908', 75.25969, 39.716633, 674);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (676, '653001', '阿图什市', 3, '0908', 76.173939, 39.712898, 674);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (677, '653023', '阿合奇县', 3, '0908', 78.450164, 40.937567, 674);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (678, '653022', '阿克陶县', 3, '0908', 75.945159, 39.147079, 674);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (679, '652900', '阿克苏地区', 2, '0997', 80.265068, 41.170712, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (680, '652922', '温宿县', 3, '0997', 80.243273, 41.272995, 679);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (681, '652901', '阿克苏市', 3, '0997', 80.2629, 41.171272, 679);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (682, '652902', '库车市', 3, '0997', 82.96304, 41.717141, 679);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (683, '652924', '沙雅县', 3, '0997', 82.78077, 41.226268, 679);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (684, '652926', '拜城县', 3, '0997', 81.869881, 41.796101, 679);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (685, '652929', '柯坪县', 3, '0997', 79.04785, 40.50624, 679);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (686, '652925', '新和县', 3, '0997', 82.610828, 41.551176, 679);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (687, '652928', '阿瓦提县', 3, '0997', 80.378426, 40.638422, 679);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (688, '652927', '乌什县', 3, '0997', 79.230805, 41.21587, 679);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (689, '650200', '克拉玛依市', 2, '0990', 84.873946, 45.595886, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (690, '650204', '白碱滩区', 3, '0990', 85.129882, 45.689021, 689);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (691, '650203', '克拉玛依区', 3, '0990', 84.868918, 45.600477, 689);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (692, '650205', '乌尔禾区', 3, '0990', 85.697767, 46.08776, 689);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (693, '650202', '独山子区', 3, '0990', 84.882267, 44.327207, 689);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (694, '659010', '胡杨河市', 2, '0992', 84.8275959, 44.69288853, 574);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (695, '210000', '辽宁省', 1, '', 123.429096, 41.796767, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (696, '210700', '锦州市', 2, '0416', 121.135742, 41.119269, 695);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (697, '210727', '义县', 3, '0416', 121.242831, 41.537224, 696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (698, '210726', '黑山县', 3, '0416', 122.117915, 41.691804, 696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (699, '210781', '凌海市', 3, '0416', 121.364236, 41.171738, 696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (700, '210711', '太和区', 3, '0416', 121.107297, 41.105378, 696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (701, '210782', '北镇市', 3, '0416', 121.795962, 41.598764, 696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (702, '210703', '凌河区', 3, '0416', 121.151304, 41.114662, 696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (703, '210702', '古塔区', 3, '0416', 121.130085, 41.115719, 696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (704, '210200', '大连市', 2, '0411', 121.618622, 38.91459, 695);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (705, '210212', '旅顺口区', 3, '0411', 121.26713, 38.812043, 704);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (706, '210213', '金州区', 3, '0411', 121.789413, 39.052745, 704);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (707, '210281', '瓦房店市', 3, '0411', 122.002656, 39.63065, 704);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (708, '210224', '长海县', 3, '0411', 122.587824, 39.272399, 704);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (709, '210211', '甘井子区', 3, '0411', 121.582614, 38.975148, 704);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (710, '210214', '普兰店区', 3, '0411', 121.9705, 39.401555, 704);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (711, '210283', '庄河市', 3, '0411', 122.970612, 39.69829, 704);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (712, '210202', '中山区', 3, '0411', 121.64376, 38.921553, 704);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (713, '210203', '西岗区', 3, '0411', 121.616112, 38.914266, 704);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (714, '210204', '沙河口区', 3, '0411', 121.593702, 38.912859, 704);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (715, '210600', '丹东市', 2, '0415', 124.383044, 40.124296, 695);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (716, '210682', '凤城市', 3, '0415', 124.071067, 40.457567, 715);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (717, '210604', '振安区', 3, '0415', 124.427709, 40.158557, 715);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (718, '210681', '东港市', 3, '0415', 124.149437, 39.883467, 715);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (719, '210603', '振兴区', 3, '0415', 124.361153, 40.102801, 715);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (720, '210624', '宽甸满族自治县', 3, '0415', 124.784867, 40.730412, 715);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (721, '210602', '元宝区', 3, '0415', 124.397814, 40.136483, 715);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (722, '210400', '抚顺市', 2, '0413', 123.921109, 41.875956, 695);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (723, '210423', '清原满族自治县', 3, '0413', 124.927192, 42.10135, 722);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (724, '210422', '新宾满族自治县', 3, '0413', 125.037547, 41.732456, 722);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (725, '210411', '顺城区', 3, '0413', 123.917165, 41.881132, 722);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (726, '210403', '东洲区', 3, '0413', 124.047219, 41.866829, 722);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (727, '210402', '新抚区', 3, '0413', 123.902858, 41.86082, 722);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (728, '210404', '望花区', 3, '0413', 123.801509, 41.851803, 722);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (729, '210421', '抚顺县', 3, '0413', 124.097979, 41.922644, 722);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (730, '211400', '葫芦岛市', 2, '0429', 120.856394, 40.755572, 695);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (731, '211403', '龙港区', 3, '0429', 120.838569, 40.709991, 730);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (732, '211422', '建昌县', 3, '0429', 119.807776, 40.812871, 730);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (733, '211481', '兴城市', 3, '0429', 120.729365, 40.619413, 730);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (734, '211421', '绥中县', 3, '0429', 120.342112, 40.328407, 730);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (735, '211402', '连山区', 3, '0429', 120.85937, 40.755143, 730);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (736, '211404', '南票区', 3, '0429', 120.752314, 41.098813, 730);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (737, '211200', '铁岭市', 2, '0410', 123.844279, 42.290585, 695);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (738, '211282', '开原市', 3, '0410', 124.045551, 42.542141, 737);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (739, '211281', '调兵山市', 3, '0410', 123.545366, 42.450734, 737);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (740, '211204', '清河区', 3, '0410', 124.14896, 42.542978, 737);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (741, '211202', '银州区', 3, '0410', 123.844877, 42.292278, 737);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (742, '211223', '西丰县', 3, '0410', 124.72332, 42.738091, 737);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (743, '211221', '铁岭县', 3, '0410', 123.725669, 42.223316, 737);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (744, '211224', '昌图县', 3, '0410', 124.11017, 42.784441, 737);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (745, '210100', '沈阳市', 2, '024', 123.429096, 41.796767, 695);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (746, '210181', '新民市', 3, '024', 122.828868, 41.996508, 745);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (747, '210111', '苏家屯区', 3, '024', 123.341604, 41.665904, 745);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (748, '210102', '和平区', 3, '024', 123.406664, 41.788074, 745);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (749, '210105', '皇姑区', 3, '024', 123.405677, 41.822336, 745);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (750, '210114', '于洪区', 3, '024', 123.310829, 41.795833, 745);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (751, '210123', '康平县', 3, '024', 123.352703, 42.741533, 745);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (752, '210124', '法库县', 3, '024', 123.416722, 42.507045, 745);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (753, '210103', '沈河区', 3, '024', 123.445696, 41.795591, 745);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (754, '210104', '大东区', 3, '024', 123.469956, 41.808503, 745);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (755, '210106', '铁西区', 3, '024', 123.350664, 41.787808, 745);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (756, '210115', '辽中区', 3, '024', 122.731269, 41.512725, 745);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (757, '210113', '沈北新区', 3, '024', 123.521471, 42.052312, 745);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (758, '210112', '浑南区', 3, '024', 123.458981, 41.741946, 745);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (759, '210800', '营口市', 2, '0417', 122.235151, 40.667432, 695);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (760, '210882', '大石桥市', 3, '0417', 122.505894, 40.633973, 759);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (761, '210804', '鲅鱼圈区', 3, '0417', 122.127242, 40.263646, 759);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (762, '210881', '盖州市', 3, '0417', 122.355534, 40.405234, 759);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (763, '210803', '西市区', 3, '0417', 122.210067, 40.663086, 759);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (764, '210802', '站前区', 3, '0417', 122.253235, 40.669949, 759);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (765, '210811', '老边区', 3, '0417', 122.382584, 40.682723, 759);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (766, '211100', '盘锦市', 2, '0427', 122.06957, 41.124484, 695);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (767, '211103', '兴隆台区', 3, '0427', 122.071624, 41.122423, 766);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (768, '211122', '盘山县', 3, '0427', 121.98528, 41.240701, 766);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (769, '211104', '大洼区', 3, '0427', 122.071708, 40.994428, 766);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (770, '211102', '双台子区', 3, '0427', 122.055733, 41.190365, 766);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (771, '210900', '阜新市', 2, '0418', 121.648962, 42.011796, 695);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (772, '210905', '清河门区', 3, '0418', 121.42018, 41.780477, 771);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (773, '210904', '太平区', 3, '0418', 121.677575, 42.011145, 771);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (774, '210921', '阜新蒙古族自治县', 3, '0418', 121.743125, 42.058607, 771);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (775, '210903', '新邱区', 3, '0418', 121.790541, 42.086603, 771);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (776, '210922', '彰武县', 3, '0418', 122.537444, 42.384823, 771);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (777, '210902', '海州区', 3, '0418', 121.657639, 42.011162, 771);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (778, '210911', '细河区', 3, '0418', 121.654791, 42.019218, 771);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (779, '210500', '本溪市', 2, '0414', 123.770519, 41.297909, 695);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (780, '210522', '桓仁满族自治县', 3, '0414', 125.359195, 41.268997, 779);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (781, '210503', '溪湖区', 3, '0414', 123.765226, 41.330056, 779);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (782, '210505', '南芬区', 3, '0414', 123.748381, 41.104093, 779);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (783, '210502', '平山区', 3, '0414', 123.761231, 41.291581, 779);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (784, '210504', '明山区', 3, '0414', 123.763288, 41.302429, 779);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (785, '210521', '本溪满族自治县', 3, '0414', 124.126156, 41.300344, 779);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (786, '211000', '辽阳市', 2, '0419', 123.18152, 41.269402, 695);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (787, '211005', '弓长岭区', 3, '0419', 123.431633, 41.157831, 786);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (788, '211081', '灯塔市', 3, '0419', 123.325864, 41.427836, 786);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (789, '211021', '辽阳县', 3, '0419', 123.079674, 41.216479, 786);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (790, '211002', '白塔区', 3, '0419', 123.172611, 41.26745, 786);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (791, '211003', '文圣区', 3, '0419', 123.188227, 41.266765, 786);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (792, '211011', '太子河区', 3, '0419', 123.185336, 41.251682, 786);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (793, '211004', '宏伟区', 3, '0419', 123.200461, 41.205747, 786);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (794, '210300', '鞍山市', 2, '0412', 122.995632, 41.110626, 695);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (795, '210321', '台安县', 3, '0412', 122.429736, 41.38686, 794);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (796, '210323', '岫岩满族自治县', 3, '0412', 123.28833, 40.281509, 794);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (797, '210311', '千山区', 3, '0412', 122.949298, 41.068909, 794);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (798, '210303', '铁西区', 3, '0412', 122.971834, 41.11069, 794);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (799, '210302', '铁东区', 3, '0412', 122.994475, 41.110344, 794);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (800, '210304', '立山区', 3, '0412', 123.024806, 41.150622, 794);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (801, '210381', '海城市', 3, '0412', 122.752199, 40.852533, 794);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (802, '211300', '朝阳市', 2, '0421', 120.451176, 41.576758, 695);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (803, '211322', '建平县', 3, '0421', 119.642363, 41.402576, 802);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (804, '211381', '北票市', 3, '0421', 120.766951, 41.803286, 802);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (805, '211321', '朝阳县', 3, '0421', 120.404217, 41.526342, 802);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (806, '211302', '双塔区', 3, '0421', 120.44877, 41.579389, 802);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (807, '211303', '龙城区', 3, '0421', 120.413376, 41.576749, 802);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (808, '211324', '喀喇沁左翼蒙古族自治县', 3, '0421', 119.744883, 41.125428, 802);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (809, '211382', '凌源市', 3, '0421', 119.404789, 41.243086, 802);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (810, '420000', '湖北省', 1, '', 114.298572, 30.584355, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (811, '420300', '十堰市', 2, '0719', 110.787916, 32.646907, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (812, '420381', '丹江口市', 3, '0719', 111.513793, 32.538839, 811);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (813, '420304', '郧阳区', 3, '0719', 110.812099, 32.838267, 811);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (814, '420303', '张湾区', 3, '0719', 110.772365, 32.652516, 811);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (815, '420302', '茅箭区', 3, '0719', 110.78621, 32.644463, 811);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (816, '420324', '竹溪县', 3, '0719', 109.717196, 32.315342, 811);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (817, '420323', '竹山县', 3, '0719', 110.2296, 32.22586, 811);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (818, '420325', '房县', 3, '0719', 110.741966, 32.055002, 811);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (819, '420322', '郧西县', 3, '0719', 110.426472, 32.991457, 811);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (820, '420600', '襄阳市', 2, '0710', 112.144146, 32.042426, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (821, '420684', '宜城市', 3, '0710', 112.261441, 31.709203, 820);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (822, '420626', '保康县', 3, '0710', 111.262235, 31.873507, 820);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (823, '420683', '枣阳市', 3, '0710', 112.765268, 32.123083, 820);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (824, '420625', '谷城县', 3, '0710', 111.640147, 32.262676, 820);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (825, '420624', '南漳县', 3, '0710', 111.844424, 31.77692, 820);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (826, '420682', '老河口市', 3, '0710', 111.675732, 32.385438, 820);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (827, '420602', '襄城区', 3, '0710', 112.150327, 32.015088, 820);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (828, '420606', '樊城区', 3, '0710', 112.13957, 32.058589, 820);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (829, '420607', '襄州区', 3, '0710', 112.197378, 32.085517, 820);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (830, '420500', '宜昌市', 2, '0717', 111.290843, 30.702636, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (831, '420525', '远安县', 3, '0717', 111.64331, 31.059626, 830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (832, '420526', '兴山县', 3, '0717', 110.754499, 31.34795, 830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (833, '420582', '当阳市', 3, '0717', 111.793419, 30.824492, 830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (834, '420527', '秭归县', 3, '0717', 110.976785, 30.823908, 830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (835, '420528', '长阳土家族自治县', 3, '0717', 111.198475, 30.466534, 830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (836, '420504', '点军区', 3, '0717', 111.268163, 30.692322, 830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (837, '420505', '猇亭区', 3, '0717', 111.427642, 30.530744, 830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (838, '420503', '伍家岗区', 3, '0717', 111.307215, 30.679053, 830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (839, '420581', '宜都市', 3, '0717', 111.454367, 30.387234, 830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (840, '420529', '五峰土家族自治县', 3, '0717', 110.674938, 30.199252, 830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (841, '420583', '枝江市', 3, '0717', 111.751799, 30.425364, 830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (842, '420506', '夷陵区', 3, '0717', 111.326747, 30.770199, 830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (843, '420502', '西陵区', 3, '0717', 111.295468, 30.702476, 830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (844, '420800', '荆门市', 2, '0724', 112.204251, 31.03542, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (845, '420802', '东宝区', 3, '0724', 112.204804, 31.033461, 844);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (846, '420881', '钟祥市', 3, '0724', 112.587267, 31.165573, 844);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (847, '420882', '京山市', 3, '0724', 113.114595, 31.022457, 844);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (848, '420804', '掇刀区', 3, '0724', 112.198413, 30.980798, 844);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (849, '420822', '沙洋县', 3, '0724', 112.595218, 30.70359, 844);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (850, '421100', '黄冈市', 2, '0713', 114.879365, 30.447711, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (851, '421181', '麻城市', 3, '0713', 115.02541, 31.177906, 850);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (852, '421121', '团风县', 3, '0713', 114.872029, 30.63569, 850);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (853, '421102', '黄州区', 3, '0713', 114.878934, 30.447435, 850);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (854, '421126', '蕲春县', 3, '0713', 115.433964, 30.234927, 850);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (855, '421125', '浠水县', 3, '0713', 115.26344, 30.454837, 850);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (856, '421124', '英山县', 3, '0713', 115.67753, 30.735794, 850);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (857, '421122', '红安县', 3, '0713', 114.615095, 31.284777, 850);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (858, '421127', '黄梅县', 3, '0713', 115.942548, 30.075113, 850);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (859, '421182', '武穴市', 3, '0713', 115.56242, 29.849342, 850);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (860, '421123', '罗田县', 3, '0713', 115.398984, 30.781679, 850);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (861, '429006', '天门市', 2, '1728', 113.165862, 30.653061, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (862, '420900', '孝感市', 2, '0712', 113.926655, 30.926423, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (863, '420923', '云梦县', 3, '0712', 113.750616, 31.021691, 862);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (864, '420981', '应城市', 3, '0712', 113.573842, 30.939038, 862);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (865, '420902', '孝南区', 3, '0712', 113.925849, 30.925966, 862);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (866, '420984', '汉川市', 3, '0712', 113.835301, 30.652165, 862);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (867, '420921', '孝昌县', 3, '0712', 113.988964, 31.251618, 862);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (868, '420922', '大悟县', 3, '0712', 114.126249, 31.565483, 862);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (869, '420982', '安陆市', 3, '0712', 113.690401, 31.26174, 862);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (870, '429005', '潜江市', 2, '2728', 112.896866, 30.421215, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (871, '422800', '恩施土家族苗族自治州', 2, '0718', 109.48699, 30.283114, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (872, '422822', '建始县', 3, '0718', 109.723822, 30.601632, 871);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (873, '422801', '恩施市', 3, '0718', 109.486761, 30.282406, 871);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (874, '422802', '利川市', 3, '0718', 108.943491, 30.294247, 871);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (875, '422828', '鹤峰县', 3, '0718', 110.033699, 29.887298, 871);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (876, '422827', '来凤县', 3, '0718', 109.408328, 29.506945, 871);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (877, '422823', '巴东县', 3, '0718', 110.336665, 31.041403, 871);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (878, '422825', '宣恩县', 3, '0718', 109.482819, 29.98867, 871);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (879, '422826', '咸丰县', 3, '0718', 109.15041, 29.678967, 871);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (880, '420100', '武汉市', 2, '027', 114.298572, 30.584355, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (881, '420116', '黄陂区', 3, '027', 114.374025, 30.874155, 880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (882, '420117', '新洲区', 3, '027', 114.802108, 30.842149, 880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (883, '420114', '蔡甸区', 3, '027', 114.029341, 30.582186, 880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (884, '420113', '汉南区', 3, '027', 114.08124, 30.309637, 880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (885, '420106', '武昌区', 3, '027', 114.307344, 30.546536, 880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (886, '420115', '江夏区', 3, '027', 114.313961, 30.349045, 880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (887, '420112', '东西湖区', 3, '027', 114.142483, 30.622467, 880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (888, '420104', '硚口区', 3, '027', 114.264568, 30.57061, 880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (889, '420105', '汉阳区', 3, '027', 114.265807, 30.549326, 880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (890, '420103', '江汉区', 3, '027', 114.283109, 30.578771, 880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (891, '420102', '江岸区', 3, '027', 114.30304, 30.594911, 880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (892, '420107', '青山区', 3, '027', 114.39707, 30.634215, 880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (893, '420111', '洪山区', 3, '027', 114.400718, 30.504259, 880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (894, '429004', '仙桃市', 2, '0728', 113.453974, 30.364953, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (895, '421000', '荆州市', 2, '0716', 112.23813, 30.326857, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (896, '421024', '江陵县', 3, '0716', 112.41735, 30.033919, 895);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (897, '421023', '监利县', 3, '0716', 112.904344, 29.820079, 895);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (898, '421083', '洪湖市', 3, '0716', 113.470304, 29.81297, 895);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (899, '421081', '石首市', 3, '0716', 112.40887, 29.716437, 895);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (900, '421087', '松滋市', 3, '0716', 111.77818, 30.176037, 895);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (901, '421002', '沙市区', 3, '0716', 112.257433, 30.315895, 895);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (902, '421022', '公安县', 3, '0716', 112.230179, 30.059065, 895);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (903, '421003', '荆州区', 3, '0716', 112.195354, 30.350674, 895);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (904, '421300', '随州市', 2, '0722', 113.37377, 31.717497, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (905, '421303', '曾都区', 3, '0722', 113.374519, 31.717521, 904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (906, '421321', '随县', 3, '0722', 113.301384, 31.854246, 904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (907, '421381', '广水市', 3, '0722', 113.826601, 31.617731, 904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (908, '429021', '神农架林区', 2, '1719', 110.671525, 31.744449, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (909, '421200', '咸宁市', 2, '0715', 114.328963, 29.832798, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (910, '421221', '嘉鱼县', 3, '0715', 113.921547, 29.973363, 909);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (911, '421224', '通山县', 3, '0715', 114.493163, 29.604455, 909);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (912, '421202', '咸安区', 3, '0715', 114.333894, 29.824716, 909);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (913, '421223', '崇阳县', 3, '0715', 114.049958, 29.54101, 909);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (914, '421281', '赤壁市', 3, '0715', 113.88366, 29.716879, 909);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (915, '421222', '通城县', 3, '0715', 113.814131, 29.246076, 909);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (916, '420700', '鄂州市', 2, '0711', 114.890593, 30.396536, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (917, '420703', '华容区', 3, '0711', 114.74148, 30.534468, 916);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (918, '420704', '鄂城区', 3, '0711', 114.890012, 30.39669, 916);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (919, '420702', '梁子湖区', 3, '0711', 114.681967, 30.098191, 916);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (920, '420200', '黄石市', 2, '0714', 115.077048, 30.220074, 810);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (921, '420205', '铁山区', 3, '0714', 114.901366, 30.20601, 920);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (922, '420203', '西塞山区', 3, '0714', 115.093354, 30.205365, 920);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (923, '420204', '下陆区', 3, '0714', 114.975755, 30.177845, 920);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (924, '420281', '大冶市', 3, '0714', 114.974842, 30.098804, 920);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (925, '420222', '阳新县', 3, '0714', 115.212883, 29.841572, 920);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (926, '420202', '黄石港区', 3, '0714', 115.090164, 30.212086, 920);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (927, '610000', '陕西省', 1, '', 108.948024, 34.263161, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (928, '611000', '商洛市', 2, '0914', 109.939776, 33.868319, 927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (929, '611022', '丹凤县', 3, '0914', 110.33191, 33.694711, 928);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (930, '611025', '镇安县', 3, '0914', 109.151075, 33.423981, 928);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (931, '611026', '柞水县', 3, '0914', 109.111249, 33.682773, 928);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (932, '611023', '商南县', 3, '0914', 110.885437, 33.526367, 928);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (933, '611021', '洛南县', 3, '0914', 110.145716, 34.088502, 928);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (934, '611002', '商州区', 3, '0914', 109.937685, 33.869208, 928);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (935, '611024', '山阳县', 3, '0914', 109.880435, 33.530411, 928);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (936, '610200', '铜川市', 2, '0919', 108.979608, 34.916582, 927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (937, '610222', '宜君县', 3, '0919', 109.118278, 35.398766, 936);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (938, '610202', '王益区', 3, '0919', 109.075862, 35.069098, 936);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (939, '610204', '耀州区', 3, '0919', 108.962538, 34.910206, 936);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (940, '610203', '印台区', 3, '0919', 109.100814, 35.111927, 936);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (941, '610100', '西安市', 2, '029', 108.948024, 34.263161, 927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (942, '610115', '临潼区', 3, '029', 109.213986, 34.372065, 941);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (943, '610117', '高陵区', 3, '029', 109.088896, 34.535065, 941);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (944, '610122', '蓝田县', 3, '029', 109.317634, 34.156189, 941);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (945, '610118', '鄠邑区', 3, '029', 108.607385, 34.108668, 941);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (946, '610116', '长安区', 3, '029', 108.941579, 34.157097, 941);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (947, '610104', '莲湖区', 3, '029', 108.933194, 34.2656, 941);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (948, '610111', '灞桥区', 3, '029', 109.067261, 34.267453, 941);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (949, '610102', '新城区', 3, '029', 108.959903, 34.26927, 941);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (950, '610103', '碑林区', 3, '029', 108.946994, 34.251061, 941);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (951, '610124', '周至县', 3, '029', 108.216465, 34.161532, 941);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (952, '610113', '雁塔区', 3, '029', 108.926593, 34.213389, 941);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (953, '610114', '阎良区', 3, '029', 109.22802, 34.662141, 941);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (954, '610112', '未央区', 3, '029', 108.946022, 34.30823, 941);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (955, '610700', '汉中市', 2, '0916', 107.028621, 33.077668, 927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (956, '610730', '佛坪县', 3, '0916', 107.988582, 33.520745, 955);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (957, '610723', '洋县', 3, '0916', 107.549962, 33.223283, 955);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (958, '610729', '留坝县', 3, '0916', 106.924377, 33.61334, 955);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (959, '610727', '略阳县', 3, '0916', 106.153899, 33.329638, 955);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (960, '610724', '西乡县', 3, '0916', 107.765858, 32.987961, 955);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (961, '610726', '宁强县', 3, '0916', 106.25739, 32.830806, 955);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (962, '610725', '勉县', 3, '0916', 106.680175, 33.155618, 955);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (963, '610703', '南郑区', 3, '0916', 106.942393, 33.003341, 955);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (964, '610702', '汉台区', 3, '0916', 107.028233, 33.077674, 955);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (965, '610722', '城固县', 3, '0916', 107.329887, 33.153098, 955);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (966, '610728', '镇巴县', 3, '0916', 107.89531, 32.535854, 955);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (967, '610800', '榆林市', 2, '0912', 109.741193, 38.290162, 927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (968, '610802', '榆阳区', 3, '0912', 109.74791, 38.299267, 967);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (969, '610827', '米脂县', 3, '0912', 110.178683, 37.759081, 967);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (970, '610828', '佳县', 3, '0912', 110.493367, 38.021597, 967);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (971, '610822', '府谷县', 3, '0912', 111.069645, 39.029243, 967);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (972, '610803', '横山区', 3, '0912', 109.292596, 37.964048, 967);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (973, '610881', '神木市', 3, '0912', 110.497005, 38.835641, 967);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (974, '610824', '靖边县', 3, '0912', 108.80567, 37.596084, 967);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (975, '610831', '子洲县', 3, '0912', 110.03457, 37.611573, 967);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (976, '610830', '清涧县', 3, '0912', 110.12146, 37.087702, 967);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (977, '610829', '吴堡县', 3, '0912', 110.739315, 37.451925, 967);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (978, '610826', '绥德县', 3, '0912', 110.265377, 37.507701, 967);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (979, '610825', '定边县', 3, '0912', 107.601284, 37.59523, 967);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (980, '610600', '延安市', 2, '0911', 109.49081, 36.596537, 927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (981, '610603', '安塞区', 3, '0911', 109.325341, 36.86441, 980);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (982, '610626', '吴起县', 3, '0911', 108.176976, 36.924852, 980);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (983, '610625', '志丹县', 3, '0911', 108.768898, 36.823031, 980);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (984, '610622', '延川县', 3, '0911', 110.190314, 36.882066, 980);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (985, '610602', '宝塔区', 3, '0911', 109.49069, 36.596291, 980);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (986, '610681', '子长市', 3, '0911', 109.675968, 37.14207, 980);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (987, '610621', '延长县', 3, '0911', 110.012961, 36.578306, 980);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (988, '610629', '洛川县', 3, '0911', 109.435712, 35.762133, 980);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (989, '610632', '黄陵县', 3, '0911', 109.262469, 35.580165, 980);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (990, '610630', '宜川县', 3, '0911', 110.175537, 36.050391, 980);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (991, '610627', '甘泉县', 3, '0911', 109.34961, 36.277729, 980);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (992, '610628', '富县', 3, '0911', 109.384136, 35.996495, 980);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (993, '610631', '黄龙县', 3, '0911', 109.83502, 35.583276, 980);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (994, '610300', '宝鸡市', 2, '0917', 107.14487, 34.369315, 927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (995, '610329', '麟游县', 3, '0917', 107.796608, 34.677714, 994);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (996, '610327', '陇县', 3, '0917', 106.857066, 34.893262, 994);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (997, '610328', '千阳县', 3, '0917', 107.132987, 34.642584, 994);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (998, '610303', '金台区', 3, '0917', 107.149943, 34.375192, 994);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (999, '610330', '凤县', 3, '0917', 106.525212, 33.912464, 994);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1000, '610323', '岐山县', 3, '0917', 107.624464, 34.44296, 994);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1001, '610302', '渭滨区', 3, '0917', 107.144467, 34.371008, 994);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1002, '610331', '太白县', 3, '0917', 107.316533, 34.059215, 994);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1003, '610304', '陈仓区', 3, '0917', 107.383645, 34.352747, 994);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1004, '610322', '凤翔县', 3, '0917', 107.400577, 34.521668, 994);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1005, '610324', '扶风县', 3, '0917', 107.891419, 34.375497, 994);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1006, '610326', '眉县', 3, '0917', 107.752371, 34.272137, 994);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1007, '610500', '渭南市', 2, '0913', 109.502882, 34.499381, 927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1008, '610527', '白水县', 3, '0913', 109.594309, 35.177291, 1007);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1009, '610581', '韩城市', 3, '0913', 110.452391, 35.475238, 1007);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1010, '610523', '大荔县', 3, '0913', 109.943123, 34.795011, 1007);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1011, '610582', '华阴市', 3, '0913', 110.08952, 34.565359, 1007);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1012, '610503', '华州区', 3, '0913', 109.76141, 34.511958, 1007);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1013, '610522', '潼关县', 3, '0913', 110.24726, 34.544515, 1007);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1014, '610525', '澄城县', 3, '0913', 109.937609, 35.184, 1007);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1015, '610526', '蒲城县', 3, '0913', 109.589653, 34.956034, 1007);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1016, '610502', '临渭区', 3, '0913', 109.503299, 34.501271, 1007);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1017, '610524', '合阳县', 3, '0913', 110.147979, 35.237098, 1007);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1018, '610528', '富平县', 3, '0913', 109.187174, 34.746679, 1007);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1019, '610400', '咸阳市', 2, '0910', 108.705117, 34.333439, 927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1020, '610429', '旬邑县', 3, '0910', 108.337231, 35.112234, 1019);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1021, '610482', '彬州市', 3, '0910', 108.083674, 35.034233, 1019);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1022, '610424', '乾县', 3, '0910', 108.247406, 34.527261, 1019);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1023, '610426', '永寿县', 3, '0910', 108.143129, 34.692619, 1019);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1024, '610404', '渭城区', 3, '0910', 108.730957, 34.336847, 1019);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1025, '610431', '武功县', 3, '0910', 108.212857, 34.259732, 1019);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1026, '610423', '泾阳县', 3, '0910', 108.83784, 34.528493, 1019);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1027, '610428', '长武县', 3, '0910', 107.795835, 35.206122, 1019);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1028, '610481', '兴平市', 3, '0910', 108.488493, 34.297134, 1019);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1029, '610403', '杨陵区', 3, '0910', 108.086348, 34.27135, 1019);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1030, '610422', '三原县', 3, '0910', 108.943481, 34.613996, 1019);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1031, '610430', '淳化县', 3, '0910', 108.581173, 34.79797, 1019);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1032, '610402', '秦都区', 3, '0910', 108.698636, 34.329801, 1019);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1033, '610425', '礼泉县', 3, '0910', 108.428317, 34.482583, 1019);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1034, '610900', '安康市', 2, '0915', 109.029273, 32.6903, 927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1035, '610923', '宁陕县', 3, '0915', 108.313714, 33.312184, 1034);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1036, '610921', '汉阴县', 3, '0915', 108.510946, 32.891121, 1034);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1037, '610922', '石泉县', 3, '0915', 108.250512, 33.038512, 1034);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1038, '610924', '紫阳县', 3, '0915', 108.537788, 32.520176, 1034);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1039, '610902', '汉滨区', 3, '0915', 109.029098, 32.690817, 1034);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1040, '610928', '旬阳县', 3, '0915', 109.368149, 32.833567, 1034);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1041, '610926', '平利县', 3, '0915', 109.361865, 32.387933, 1034);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1042, '610929', '白河县', 3, '0915', 110.114186, 32.809484, 1034);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1043, '610925', '岚皋县', 3, '0915', 108.900663, 32.31069, 1034);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1044, '610927', '镇坪县', 3, '0915', 109.526437, 31.883395, 1034);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1045, '520000', '贵州省', 1, '', 106.713478, 26.578343, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1046, '520200', '六盘水市', 2, '0858', 104.846743, 26.584643, 1045);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1047, '520201', '钟山区', 3, '0858', 104.846244, 26.584805, 1046);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1048, '520203', '六枝特区', 3, '0858', 105.474235, 26.210662, 1046);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1049, '520281', '盘州市', 3, '0858', 104.468367, 25.706966, 1046);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1050, '520221', '水城县', 3, '0858', 104.95685, 26.540478, 1046);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1051, '520600', '铜仁市', 2, '0856', 109.191555, 27.718346, 1045);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1052, '520623', '石阡县', 3, '0856', 108.229854, 27.519386, 1051);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1053, '520625', '印江土家族苗族自治县', 3, '0856', 108.405517, 27.997976, 1051);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1054, '520626', '德江县', 3, '0856', 108.117317, 28.26094, 1051);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1055, '520602', '碧江区', 3, '0856', 109.192117, 27.718745, 1051);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1056, '520621', '江口县', 3, '0856', 108.848427, 27.691904, 1051);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1057, '520603', '万山区', 3, '0856', 109.21199, 27.51903, 1051);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1058, '520628', '松桃苗族自治县', 3, '0856', 109.202627, 28.165419, 1051);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1059, '520627', '沿河土家族自治县', 3, '0856', 108.495746, 28.560487, 1051);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1060, '520624', '思南县', 3, '0856', 108.255827, 27.941331, 1051);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1061, '520622', '玉屏侗族自治县', 3, '0856', 108.917882, 27.238024, 1051);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1062, '520300', '遵义市', 2, '0852', 106.937265, 27.706626, 1045);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1063, '520323', '绥阳县', 3, '0852', 107.191024, 27.951342, 1062);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1064, '520328', '湄潭县', 3, '0852', 107.485723, 27.765839, 1062);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1065, '520322', '桐梓县', 3, '0852', 106.826591, 28.131559, 1062);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1066, '520329', '余庆县', 3, '0852', 107.892566, 27.221552, 1062);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1067, '520304', '播州区', 3, '0852', 106.831668, 27.535288, 1062);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1068, '520382', '仁怀市', 3, '0852', 106.412476, 27.803377, 1062);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1069, '520381', '赤水市', 3, '0852', 105.698116, 28.587057, 1062);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1070, '520330', '习水县', 3, '0852', 106.200954, 28.327826, 1062);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1071, '520325', '道真仡佬族苗族自治县', 3, '0852', 107.605342, 28.880088, 1062);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1072, '520324', '正安县', 3, '0852', 107.441872, 28.550337, 1062);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1073, '520326', '务川仡佬族苗族自治县', 3, '0852', 107.887857, 28.521567, 1062);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1074, '520327', '凤冈县', 3, '0852', 107.722021, 27.960858, 1062);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1075, '520303', '汇川区', 3, '0852', 106.937265, 27.706626, 1062);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1076, '520302', '红花岗区', 3, '0852', 106.943784, 27.694395, 1062);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1077, '520400', '安顺市', 2, '0853', 105.932188, 26.245544, 1045);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1078, '520402', '西秀区', 3, '0853', 105.946169, 26.248323, 1077);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1079, '520425', '紫云苗族布依族自治县', 3, '0853', 106.084515, 25.751567, 1077);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1080, '520423', '镇宁布依族苗族自治县', 3, '0853', 105.768656, 26.056096, 1077);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1081, '520424', '关岭布依族苗族自治县', 3, '0853', 105.618454, 25.944248, 1077);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1082, '520403', '平坝区', 3, '0853', 106.259942, 26.40608, 1077);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1083, '520422', '普定县', 3, '0853', 105.745609, 26.305794, 1077);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1084, '522700', '黔南布依族苗族自治州', 2, '0854', 107.517156, 26.258219, 1045);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1085, '522729', '长顺县', 3, '0854', 106.447376, 26.022116, 1084);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1086, '522726', '独山县', 3, '0854', 107.542757, 25.826283, 1084);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1087, '522731', '惠水县', 3, '0854', 106.657848, 26.128637, 1084);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1088, '522727', '平塘县', 3, '0854', 107.32405, 25.831803, 1084);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1089, '522728', '罗甸县', 3, '0854', 106.750006, 25.429894, 1084);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1090, '522722', '荔波县', 3, '0854', 107.8838, 25.412239, 1084);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1091, '522730', '龙里县', 3, '0854', 106.977733, 26.448809, 1084);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1092, '522723', '贵定县', 3, '0854', 107.233588, 26.580807, 1084);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1093, '522732', '三都水族自治县', 3, '0854', 107.87747, 25.985183, 1084);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1094, '522725', '瓮安县', 3, '0854', 107.478417, 27.066339, 1084);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1095, '522702', '福泉市', 3, '0854', 107.513508, 26.702508, 1084);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1096, '522701', '都匀市', 3, '0854', 107.517021, 26.258205, 1084);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1097, '520500', '毕节市', 2, '0857', 105.28501, 27.301693, 1045);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1098, '520502', '七星关区', 3, '0857', 105.284852, 27.302085, 1097);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1099, '520521', '大方县', 3, '0857', 105.609254, 27.143521, 1097);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1100, '520522', '黔西县', 3, '0857', 106.038299, 27.024923, 1097);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1101, '520523', '金沙县', 3, '0857', 106.222103, 27.459693, 1097);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1102, '520525', '纳雍县', 3, '0857', 105.375322, 26.769875, 1097);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1103, '520524', '织金县', 3, '0857', 105.768997, 26.668497, 1097);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1104, '520526', '威宁彝族回族苗族自治县', 3, '0857', 104.286523, 26.859099, 1097);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1105, '520527', '赫章县', 3, '0857', 104.726438, 27.119243, 1097);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1106, '522600', '黔东南苗族侗族自治州', 2, '0855', 107.977488, 26.583352, 1045);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1107, '522624', '三穗县', 3, '0855', 108.681121, 26.959884, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1108, '522632', '榕江县', 3, '0855', 108.521026, 25.931085, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1109, '522634', '雷山县', 3, '0855', 108.079613, 26.381027, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1110, '522627', '天柱县', 3, '0855', 109.212798, 26.909684, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1111, '522623', '施秉县', 3, '0855', 108.12678, 27.034657, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1112, '522625', '镇远县', 3, '0855', 108.423656, 27.050233, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1113, '522636', '丹寨县', 3, '0855', 107.794808, 26.199497, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1114, '522601', '凯里市', 3, '0855', 107.977541, 26.582964, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1115, '522631', '黎平县', 3, '0855', 109.136504, 26.230636, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1116, '522633', '从江县', 3, '0855', 108.912648, 25.747058, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1117, '522629', '剑河县', 3, '0855', 108.440499, 26.727349, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1118, '522628', '锦屏县', 3, '0855', 109.20252, 26.680625, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1119, '522630', '台江县', 3, '0855', 108.314637, 26.669138, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1120, '522622', '黄平县', 3, '0855', 107.901337, 26.896973, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1121, '522635', '麻江县', 3, '0855', 107.593172, 26.494803, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1122, '522626', '岑巩县', 3, '0855', 108.816459, 27.173244, 1106);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1123, '522300', '黔西南布依族苗族自治州', 2, '0859', 104.897971, 25.08812, 1045);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1124, '522301', '兴义市', 3, '0859', 104.897982, 25.088599, 1123);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1125, '522302', '兴仁市', 3, '0859', 105.192778, 25.431378, 1123);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1126, '522328', '安龙县', 3, '0859', 105.471498, 25.108959, 1123);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1127, '522327', '册亨县', 3, '0859', 105.81241, 24.983338, 1123);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1128, '522325', '贞丰县', 3, '0859', 105.650133, 25.385752, 1123);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1129, '522323', '普安县', 3, '0859', 104.955347, 25.786404, 1123);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1130, '522326', '望谟县', 3, '0859', 106.091563, 25.166667, 1123);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1131, '522324', '晴隆县', 3, '0859', 105.218773, 25.832881, 1123);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1132, '520100', '贵阳市', 2, '0851', 106.713478, 26.578343, 1045);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1133, '520121', '开阳县', 3, '0851', 106.969438, 27.056793, 1132);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1134, '520113', '白云区', 3, '0851', 106.633037, 26.676849, 1132);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1135, '520122', '息烽县', 3, '0851', 106.737693, 27.092665, 1132);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1136, '520123', '修文县', 3, '0851', 106.599218, 26.840672, 1132);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1137, '520181', '清镇市', 3, '0851', 106.470278, 26.551289, 1132);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1138, '520112', '乌当区', 3, '0851', 106.762123, 26.630928, 1132);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1139, '520102', '南明区', 3, '0851', 106.715963, 26.573743, 1132);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1140, '520111', '花溪区', 3, '0851', 106.670791, 26.410464, 1132);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1141, '520103', '云岩区', 3, '0851', 106.713397, 26.58301, 1132);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1142, '520115', '观山湖区', 3, '0851', 106.626323, 26.646358, 1132);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1143, '310000', '上海市', 1, '021', 121.472644, 31.231706, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1144, '310100', '上海城区', 2, '021', 121.472644, 31.231706, 1143);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1145, '310120', '奉贤区', 3, '021', 121.458472, 30.912345, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1146, '310151', '崇明区', 3, '021', 121.397516, 31.626946, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1147, '310116', '金山区', 3, '021', 121.330736, 30.724697, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1148, '310115', '浦东新区', 3, '021', 121.567706, 31.245944, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1149, '310101', '黄浦区', 3, '021', 121.490317, 31.222771, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1150, '310113', '宝山区', 3, '021', 121.489934, 31.398896, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1151, '310107', '普陀区', 3, '021', 121.392499, 31.241701, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1152, '310114', '嘉定区', 3, '021', 121.250333, 31.383524, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1153, '310110', '杨浦区', 3, '021', 121.522797, 31.270755, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1154, '310117', '松江区', 3, '021', 121.223543, 31.03047, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1155, '310109', '虹口区', 3, '021', 121.491832, 31.26097, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1156, '310106', '静安区', 3, '021', 121.448224, 31.229003, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1157, '310105', '长宁区', 3, '021', 121.4222, 31.218123, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1158, '310112', '闵行区', 3, '021', 121.375972, 31.111658, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1159, '310104', '徐汇区', 3, '021', 121.43752, 31.179973, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1160, '310118', '青浦区', 3, '021', 121.113021, 31.151209, 1144);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1161, '370000', '山东省', 1, '', 117.000923, 36.675807, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1162, '371500', '聊城市', 2, '0635', 115.980367, 36.456013, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1163, '371525', '冠县', 3, '0635', 115.444808, 36.483753, 1162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1164, '371522', '莘县', 3, '0635', 115.667291, 36.237597, 1162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1165, '371503', '茌平区', 3, '0635', 116.25335, 36.591934, 1162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1166, '371502', '东昌府区', 3, '0635', 115.980023, 36.45606, 1162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1167, '371581', '临清市', 3, '0635', 115.713462, 36.842598, 1162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1168, '371524', '东阿县', 3, '0635', 116.248855, 36.336004, 1162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1169, '371521', '阳谷县', 3, '0635', 115.784287, 36.113708, 1162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1170, '371526', '高唐县', 3, '0635', 116.229662, 36.859755, 1162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1171, '370300', '淄博市', 2, '0533', 118.047648, 36.814939, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1172, '370304', '博山区', 3, '0533', 117.85823, 36.497567, 1171);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1173, '370305', '临淄区', 3, '0533', 118.306018, 36.816657, 1171);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1174, '370322', '高青县', 3, '0533', 117.829839, 37.169581, 1171);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1175, '370321', '桓台县', 3, '0533', 118.101556, 36.959773, 1171);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1176, '370303', '张店区', 3, '0533', 118.053521, 36.807049, 1171);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1177, '370323', '沂源县', 3, '0533', 118.166161, 36.186282, 1171);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1178, '370302', '淄川区', 3, '0533', 117.967696, 36.647272, 1171);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1179, '370306', '周村区', 3, '0533', 117.851036, 36.803699, 1171);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1180, '371300', '临沂市', 2, '0539', 118.326443, 35.065282, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1181, '371323', '沂水县', 3, '0539', 118.634543, 35.787029, 1180);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1182, '371328', '蒙阴县', 3, '0539', 117.943271, 35.712435, 1180);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1183, '371326', '平邑县', 3, '0539', 117.631884, 35.511519, 1180);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1184, '371322', '郯城县', 3, '0539', 118.342963, 34.614741, 1180);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1185, '371327', '莒南县', 3, '0539', 118.838322, 35.175911, 1180);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1186, '371321', '沂南县', 3, '0539', 118.455395, 35.547002, 1180);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1187, '371302', '兰山区', 3, '0539', 118.327667, 35.061631, 1180);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1188, '371311', '罗庄区', 3, '0539', 118.284795, 34.997204, 1180);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1189, '371329', '临沭县', 3, '0539', 118.648379, 34.917062, 1180);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1190, '371312', '河东区', 3, '0539', 118.398296, 35.085004, 1180);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1191, '371325', '费县', 3, '0539', 117.968869, 35.269174, 1180);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1192, '371324', '兰陵县', 3, '0539', 118.049968, 34.855573, 1180);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1193, '370200', '青岛市', 2, '0532', 120.355173, 36.082982, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1194, '370285', '莱西市', 3, '0532', 120.526226, 36.86509, 1193);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1195, '370283', '平度市', 3, '0532', 119.959012, 36.788828, 1193);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1196, '370214', '城阳区', 3, '0532', 120.389135, 36.306833, 1193);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1197, '370215', '即墨区', 3, '0532', 120.447352, 36.390847, 1193);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1198, '370213', '李沧区', 3, '0532', 120.421236, 36.160023, 1193);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1199, '370212', '崂山区', 3, '0532', 120.467393, 36.102569, 1193);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1200, '370211', '黄岛区', 3, '0532', 119.995518, 35.875138, 1193);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1201, '370203', '市北区', 3, '0532', 120.355026, 36.083819, 1193);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1202, '370281', '胶州市', 3, '0532', 120.006202, 36.285878, 1193);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1203, '370202', '市南区', 3, '0532', 120.395966, 36.070892, 1193);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1204, '371000', '威海市', 2, '0631', 122.116394, 37.509691, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1205, '371002', '环翠区', 3, '0631', 122.116189, 37.510754, 1204);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1206, '371003', '文登区', 3, '0631', 122.057139, 37.196211, 1204);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1207, '371083', '乳山市', 3, '0631', 121.536346, 36.919622, 1204);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1208, '371082', '荣成市', 3, '0631', 122.422896, 37.160134, 1204);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1209, '370600', '烟台市', 2, '0535', 121.391382, 37.539297, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1210, '370602', '芝罘区', 3, '0535', 121.385877, 37.540925, 1209);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1211, '370614', '蓬莱区', 3, '0535', 120.759074, 37.811045, 1209);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1212, '370683', '莱州市', 3, '0535', 119.942135, 37.182725, 1209);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1213, '370687', '海阳市', 3, '0535', 121.168392, 36.780657, 1209);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1214, '370681', '龙口市', 3, '0535', 120.528328, 37.648446, 1209);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1215, '370682', '莱阳市', 3, '0535', 120.711151, 36.977037, 1209);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1216, '370685', '招远市', 3, '0535', 120.403142, 37.364919, 1209);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1217, '370611', '福山区', 3, '0535', 121.264741, 37.496875, 1209);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1218, '370613', '莱山区', 3, '0535', 121.448866, 37.473549, 1209);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1219, '370686', '栖霞市', 3, '0535', 120.834097, 37.305854, 1209);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1220, '370612', '牟平区', 3, '0535', 121.60151, 37.388356, 1209);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1221, '370700', '潍坊市', 2, '0536', 119.107078, 36.70925, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1222, '370705', '奎文区', 3, '0536', 119.137357, 36.709494, 1221);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1223, '370724', '临朐县', 3, '0536', 118.539876, 36.516371, 1221);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1224, '370702', '潍城区', 3, '0536', 119.103784, 36.710062, 1221);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1225, '370784', '安丘市', 3, '0536', 119.206886, 36.427417, 1221);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1226, '370782', '诸城市', 3, '0536', 119.403182, 35.997093, 1221);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1227, '370783', '寿光市', 3, '0536', 118.736451, 36.874411, 1221);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1228, '370703', '寒亭区', 3, '0536', 119.207866, 36.772103, 1221);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1229, '370786', '昌邑市', 3, '0536', 119.394502, 36.854937, 1221);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1230, '370725', '昌乐县', 3, '0536', 118.839995, 36.703253, 1221);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1231, '370781', '青州市', 3, '0536', 118.484693, 36.697855, 1221);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1232, '370785', '高密市', 3, '0536', 119.757033, 36.37754, 1221);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1233, '370704', '坊子区', 3, '0536', 119.166326, 36.654616, 1221);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1234, '370500', '东营市', 2, '0546', 118.66471, 37.434564, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1235, '370522', '利津县', 3, '0546', 118.248854, 37.493365, 1234);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1236, '370523', '广饶县', 3, '0546', 118.407522, 37.05161, 1234);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1237, '370502', '东营区', 3, '0546', 118.507543, 37.461567, 1234);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1238, '370503', '河口区', 3, '0546', 118.529613, 37.886015, 1234);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1239, '370505', '垦利区', 3, '0546', 118.551314, 37.588679, 1234);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1240, '371600', '滨州市', 2, '0543', 118.016974, 37.383542, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1241, '371602', '滨城区', 3, '0543', 118.020149, 37.384842, 1240);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1242, '371603', '沾化区', 3, '0543', 118.129902, 37.698456, 1240);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1243, '371681', '邹平市', 3, '0543', 117.736807, 36.87803, 1240);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1244, '371625', '博兴县', 3, '0543', 118.123096, 37.147002, 1240);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1245, '371623', '无棣县', 3, '0543', 117.616325, 37.740848, 1240);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1246, '371622', '阳信县', 3, '0543', 117.581326, 37.640492, 1240);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1247, '371621', '惠民县', 3, '0543', 117.508941, 37.483876, 1240);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1248, '371100', '日照市', 2, '0633', 119.461208, 35.428588, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1249, '371121', '五莲县', 3, '0633', 119.206745, 35.751936, 1248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1250, '371102', '东港区', 3, '0633', 119.457703, 35.426152, 1248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1251, '371122', '莒县', 3, '0633', 118.832859, 35.588115, 1248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1252, '371103', '岚山区', 3, '0633', 119.315844, 35.119794, 1248);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1253, '370400', '枣庄市', 2, '0632', 117.557964, 34.856424, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1254, '370404', '峄城区', 3, '0632', 117.586316, 34.767713, 1253);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1255, '370406', '山亭区', 3, '0632', 117.458968, 35.096077, 1253);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1256, '370403', '薛城区', 3, '0632', 117.265293, 34.79789, 1253);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1257, '370402', '市中区', 3, '0632', 117.557281, 34.856651, 1253);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1258, '370481', '滕州市', 3, '0632', 117.162098, 35.088498, 1253);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1259, '370405', '台儿庄区', 3, '0632', 117.734747, 34.564815, 1253);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1260, '371400', '德州市', 2, '0534', 116.307428, 37.453968, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1261, '371422', '宁津县', 3, '0534', 116.79372, 37.649619, 1260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1262, '371481', '乐陵市', 3, '0534', 117.216657, 37.729115, 1260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1263, '371402', '德城区', 3, '0534', 116.307076, 37.453923, 1260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1264, '371428', '武城县', 3, '0534', 116.078627, 37.209527, 1260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1265, '371423', '庆云县', 3, '0534', 117.390507, 37.777724, 1260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1266, '371427', '夏津县', 3, '0534', 116.003816, 36.950501, 1260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1267, '371425', '齐河县', 3, '0534', 116.758394, 36.795497, 1260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1268, '371482', '禹城市', 3, '0534', 116.642554, 36.934485, 1260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1269, '371424', '临邑县', 3, '0534', 116.867028, 37.192044, 1260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1270, '371403', '陵城区', 3, '0534', 116.574929, 37.332848, 1260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1271, '371426', '平原县', 3, '0534', 116.433904, 37.164465, 1260);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1272, '370100', '济南市', 2, '0531', 117.000923, 36.675807, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1273, '370117', '钢城区', 3, '0531', 117.82033, 36.058038, 1272);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1274, '370116', '莱芜区', 3, '0531', 117.675808, 36.214395, 1272);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1275, '370114', '章丘区', 3, '0531', 117.54069, 36.71209, 1272);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1276, '370105', '天桥区', 3, '0531', 116.996086, 36.693374, 1272);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1277, '370115', '济阳区', 3, '0531', 117.176035, 36.976771, 1272);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1278, '370112', '历城区', 3, '0531', 117.063744, 36.681744, 1272);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1279, '370102', '历下区', 3, '0531', 117.03862, 36.664169, 1272);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1280, '370124', '平阴县', 3, '0531', 116.455054, 36.286923, 1272);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1281, '370126', '商河县', 3, '0531', 117.156369, 37.310544, 1272);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1282, '370103', '市中区', 3, '0531', 116.99898, 36.657354, 1272);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1283, '370104', '槐荫区', 3, '0531', 116.947921, 36.668205, 1272);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1284, '370113', '长清区', 3, '0531', 116.74588, 36.561049, 1272);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1285, '371700', '菏泽市', 2, '0530', 115.469381, 35.246531, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1286, '371726', '鄄城县', 3, '0530', 115.51434, 35.560257, 1285);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1287, '371702', '牡丹区', 3, '0530', 115.470946, 35.24311, 1285);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1288, '371728', '东明县', 3, '0530', 115.098412, 35.289637, 1285);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1289, '371703', '定陶区', 3, '0530', 115.569601, 35.072701, 1285);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1290, '371725', '郓城县', 3, '0530', 115.93885, 35.594773, 1285);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1291, '371724', '巨野县', 3, '0530', 116.089341, 35.390999, 1285);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1292, '371723', '成武县', 3, '0530', 115.897349, 34.947366, 1285);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1293, '371721', '曹县', 3, '0530', 115.549482, 34.823253, 1285);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1294, '371722', '单县', 3, '0530', 116.08262, 34.790851, 1285);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1295, '370900', '泰安市', 2, '0538', 117.129063, 36.194968, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1296, '370982', '新泰市', 3, '0538', 117.766092, 35.910387, 1295);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1297, '370923', '东平县', 3, '0538', 116.461052, 35.930467, 1295);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1298, '370902', '泰山区', 3, '0538', 117.129984, 36.189313, 1295);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1299, '370911', '岱岳区', 3, '0538', 117.04353, 36.1841, 1295);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1300, '370983', '肥城市', 3, '0538', 116.763703, 36.1856, 1295);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1301, '370921', '宁阳县', 3, '0538', 116.799297, 35.76754, 1295);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1302, '370800', '济宁市', 2, '0537', 116.587245, 35.415393, 1161);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1303, '370830', '汶上县', 3, '0537', 116.487146, 35.721746, 1302);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1304, '370883', '邹城市', 3, '0537', 116.96673, 35.405259, 1302);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1305, '370831', '泗水县', 3, '0537', 117.273605, 35.653216, 1302);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1306, '370881', '曲阜市', 3, '0537', 116.991885, 35.592788, 1302);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1307, '370811', '任城区', 3, '0537', 116.595261, 35.414828, 1302);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1308, '370829', '嘉祥县', 3, '0537', 116.342885, 35.398098, 1302);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1309, '370832', '梁山县', 3, '0537', 116.08963, 35.801843, 1302);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1310, '370827', '鱼台县', 3, '0537', 116.650023, 34.997706, 1302);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1311, '370828', '金乡县', 3, '0537', 116.310364, 35.06977, 1302);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1312, '370826', '微山县', 3, '0537', 117.12861, 34.809525, 1302);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1313, '370812', '兖州区', 3, '0537', 116.828996, 35.556445, 1302);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1314, '340000', '安徽省', 1, '', 117.283042, 31.86119, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1315, '340600', '淮北市', 2, '0561', 116.794664, 33.971707, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1316, '340602', '杜集区', 3, '0561', 116.833925, 33.991218, 1315);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1317, '340604', '烈山区', 3, '0561', 116.809465, 33.889529, 1315);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1318, '340603', '相山区', 3, '0561', 116.790775, 33.970916, 1315);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1319, '340621', '濉溪县', 3, '0561', 116.767435, 33.916407, 1315);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1320, '340700', '铜陵市', 2, '0562', 117.816576, 30.929935, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1321, '340711', '郊区', 3, '0562', 117.80707, 30.908927, 1320);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1322, '340705', '铜官区', 3, '0562', 117.816167, 30.927613, 1320);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1323, '340722', '枞阳县', 3, '0562', 117.222027, 30.700615, 1320);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1324, '340706', '义安区', 3, '0562', 117.792288, 30.952338, 1320);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1325, '340800', '安庆市', 2, '0556', 117.043551, 30.50883, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1326, '340882', '潜山市', 3, '0556', 116.573665, 30.638222, 1325);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1327, '340826', '宿松县', 3, '0556', 116.120204, 30.158327, 1325);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1328, '340811', '宜秀区', 3, '0556', 117.070003, 30.541323, 1325);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1329, '340825', '太湖县', 3, '0556', 116.305225, 30.451869, 1325);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1330, '340828', '岳西县', 3, '0556', 116.360482, 30.848502, 1325);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1331, '340802', '迎江区', 3, '0556', 117.044965, 30.506375, 1325);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1332, '340881', '桐城市', 3, '0556', 116.959656, 31.050576, 1325);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1333, '340827', '望江县', 3, '0556', 116.690927, 30.12491, 1325);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1334, '340822', '怀宁县', 3, '0556', 116.828664, 30.734994, 1325);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1335, '340803', '大观区', 3, '0556', 117.034512, 30.505632, 1325);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1336, '341700', '池州市', 2, '0566', 117.489157, 30.656037, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1337, '341723', '青阳县', 3, '0566', 117.857395, 30.63818, 1336);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1338, '341721', '东至县', 3, '0566', 117.021476, 30.096568, 1336);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1339, '341702', '贵池区', 3, '0566', 117.488342, 30.657378, 1336);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1340, '341722', '石台县', 3, '0566', 117.482907, 30.210324, 1336);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1341, '340500', '马鞍山市', 2, '0555', 118.507906, 31.689362, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1342, '340506', '博望区', 3, '0555', 118.843742, 31.562321, 1341);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1343, '340521', '当涂县', 3, '0555', 118.489873, 31.556167, 1341);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1344, '340504', '雨山区', 3, '0555', 118.493104, 31.685912, 1341);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1345, '340503', '花山区', 3, '0555', 118.511308, 31.69902, 1341);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1346, '340523', '和县', 3, '0555', 118.362998, 31.716634, 1341);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1347, '340522', '含山县', 3, '0555', 118.105545, 31.727758, 1341);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1348, '341200', '阜阳市', 2, '1558', 115.819729, 32.896969, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1349, '341222', '太和县', 3, '1558', 115.627243, 33.16229, 1348);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1350, '341203', '颍东区', 3, '1558', 115.858747, 32.908861, 1348);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1351, '341221', '临泉县', 3, '1558', 115.261688, 33.062698, 1348);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1352, '341204', '颍泉区', 3, '1558', 115.804525, 32.924797, 1348);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1353, '341202', '颍州区', 3, '1558', 115.813914, 32.891238, 1348);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1354, '341225', '阜南县', 3, '1558', 115.590534, 32.638102, 1348);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1355, '341226', '颍上县', 3, '1558', 116.259122, 32.637065, 1348);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1356, '341282', '界首市', 3, '1558', 115.362117, 33.26153, 1348);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1357, '341000', '黄山市', 2, '0559', 118.317325, 29.709239, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1358, '341003', '黄山区', 3, '0559', 118.136639, 30.294517, 1357);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1359, '341023', '黟县', 3, '0559', 117.942911, 29.923812, 1357);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1360, '341024', '祁门县', 3, '0559', 117.717237, 29.853472, 1357);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1361, '341021', '歙县', 3, '0559', 118.428025, 29.867748, 1357);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1362, '341004', '徽州区', 3, '0559', 118.339743, 29.825201, 1357);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1363, '341022', '休宁县', 3, '0559', 118.188531, 29.788878, 1357);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1364, '341002', '屯溪区', 3, '0559', 118.317354, 29.709186, 1357);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1365, '341500', '六安市', 2, '0564', 116.507676, 31.752889, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1366, '341525', '霍山县', 3, '0564', 116.333078, 31.402456, 1365);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1367, '341503', '裕安区', 3, '0564', 116.494543, 31.750692, 1365);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1368, '341524', '金寨县', 3, '0564', 115.878514, 31.681624, 1365);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1369, '341502', '金安区', 3, '0564', 116.503288, 31.754491, 1365);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1370, '341504', '叶集区', 3, '0564', 115.913594, 31.84768, 1365);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1371, '341522', '霍邱县', 3, '0564', 116.278875, 32.341305, 1365);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1372, '341523', '舒城县', 3, '0564', 116.944088, 31.462848, 1365);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1373, '340400', '淮南市', 2, '0554', 117.018329, 32.647574, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1374, '340421', '凤台县', 3, '0554', 116.722769, 32.705382, 1373);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1375, '340405', '八公山区', 3, '0554', 116.841111, 32.628229, 1373);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1376, '340406', '潘集区', 3, '0554', 116.816879, 32.782117, 1373);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1377, '340402', '大通区', 3, '0554', 117.052927, 32.632066, 1373);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1378, '340404', '谢家集区', 3, '0554', 116.865354, 32.598289, 1373);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1379, '340403', '田家庵区', 3, '0554', 117.018318, 32.644342, 1373);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1380, '340422', '寿县', 3, '0554', 116.785349, 32.577304, 1373);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1381, '340300', '蚌埠市', 2, '0552', 117.363228, 32.939667, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1382, '340311', '淮上区', 3, '0552', 117.34709, 32.963147, 1381);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1383, '340322', '五河县', 3, '0552', 117.888809, 33.146202, 1381);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1384, '340321', '怀远县', 3, '0552', 117.200171, 32.956934, 1381);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1385, '340304', '禹会区', 3, '0552', 117.35259, 32.931933, 1381);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1386, '340303', '蚌山区', 3, '0552', 117.355789, 32.938066, 1381);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1387, '340302', '龙子湖区', 3, '0552', 117.382312, 32.950452, 1381);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1388, '340323', '固镇县', 3, '0552', 117.315962, 33.318679, 1381);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1389, '340200', '芜湖市', 2, '0553', 118.376451, 31.326319, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1390, '340222', '繁昌县', 3, '0553', 118.201349, 31.080896, 1389);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1391, '340203', '弋江区', 3, '0553', 118.377476, 31.313394, 1389);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1392, '340202', '镜湖区', 3, '0553', 118.376343, 31.32559, 1389);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1393, '340281', '无为市', 3, '0553', 117.911432, 31.303075, 1389);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1394, '340208', '三山区', 3, '0553', 118.233987, 31.225423, 1389);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1395, '340207', '鸠江区', 3, '0553', 118.400174, 31.362716, 1389);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1396, '340221', '芜湖县', 3, '0553', 118.572301, 31.145262, 1389);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1397, '340223', '南陵县', 3, '0553', 118.337104, 30.919638, 1389);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1398, '341100', '滁州市', 2, '0550', 118.316264, 32.303627, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1399, '341182', '明光市', 3, '0550', 117.998048, 32.781206, 1398);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1400, '341181', '天长市', 3, '0550', 119.011212, 32.6815, 1398);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1401, '341126', '凤阳县', 3, '0550', 117.562461, 32.867146, 1398);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1402, '341125', '定远县', 3, '0550', 117.683713, 32.527105, 1398);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1403, '341122', '来安县', 3, '0550', 118.433293, 32.450231, 1398);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1404, '341103', '南谯区', 3, '0550', 118.296955, 32.329841, 1398);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1405, '341102', '琅琊区', 3, '0550', 118.316475, 32.303797, 1398);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1406, '341124', '全椒县', 3, '0550', 118.268576, 32.09385, 1398);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1407, '340100', '合肥市', 2, '0551', 117.283042, 31.86119, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1408, '340124', '庐江县', 3, '0551', 117.289844, 31.251488, 1407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1409, '340111', '包河区', 3, '0551', 117.285751, 31.82956, 1407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1410, '340181', '巢湖市', 3, '0551', 117.874155, 31.600518, 1407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1411, '340123', '肥西县', 3, '0551', 117.166118, 31.719646, 1407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1412, '340104', '蜀山区', 3, '0551', 117.262072, 31.855868, 1407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1413, '340103', '庐阳区', 3, '0551', 117.283776, 31.869011, 1407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1414, '340121', '长丰县', 3, '0551', 117.164699, 32.478548, 1407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1415, '340122', '肥东县', 3, '0551', 117.463222, 31.883992, 1407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1416, '340102', '瑶海区', 3, '0551', 117.315358, 31.86961, 1407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1417, '341800', '宣城市', 2, '0563', 118.757995, 30.945667, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1418, '341882', '广德市', 3, '0563', 119.417521, 30.893116, 1417);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1419, '341824', '绩溪县', 3, '0563', 118.594705, 30.065267, 1417);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1420, '341825', '旌德县', 3, '0563', 118.543081, 30.288057, 1417);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1421, '341802', '宣州区', 3, '0563', 118.758412, 30.946003, 1417);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1422, '341881', '宁国市', 3, '0563', 118.983407, 30.626529, 1417);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1423, '341821', '郎溪县', 3, '0563', 119.185024, 31.127834, 1417);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1424, '341823', '泾县', 3, '0563', 118.412397, 30.685975, 1417);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1425, '341600', '亳州市', 2, '0558', 115.782939, 33.869338, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1426, '341602', '谯城区', 3, '0558', 115.781214, 33.869284, 1425);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1427, '341623', '利辛县', 3, '0558', 116.207782, 33.143503, 1425);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1428, '341622', '蒙城县', 3, '0558', 116.560337, 33.260814, 1425);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1429, '341621', '涡阳县', 3, '0558', 116.211551, 33.502831, 1425);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1430, '341300', '宿州市', 2, '0557', 116.984084, 33.633891, 1314);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1431, '341302', '埇桥区', 3, '0557', 116.983309, 33.633853, 1430);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1432, '341324', '泗县', 3, '0557', 117.885443, 33.47758, 1430);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1433, '341322', '萧县', 3, '0557', 116.945399, 34.183266, 1430);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1434, '341323', '灵璧县', 3, '0557', 117.551493, 33.540629, 1430);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1435, '341321', '砀山县', 3, '0557', 116.351113, 34.426247, 1430);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1436, '500000', '重庆市', 1, '023', 106.504962, 29.533155, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1437, '500200', '重庆郊县', 2, '023', 108.170255, 29.291965, 1436);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1438, '500236', '奉节县', 3, '023', 109.465774, 31.019967, 1437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1439, '500238', '巫溪县', 3, '023', 109.628912, 31.3966, 1437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1440, '500229', '城口县', 3, '023', 108.6649, 31.946293, 1437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1441, '500230', '丰都县', 3, '023', 107.73248, 29.866424, 1437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1442, '500243', '彭水苗族土家族自治县', 3, '023', 108.166551, 29.293856, 1437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1443, '500241', '秀山土家族苗族自治县', 3, '023', 108.996043, 28.444772, 1437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1444, '500235', '云阳县', 3, '023', 108.697698, 30.930529, 1437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1445, '500237', '巫山县', 3, '023', 109.878928, 31.074843, 1437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1446, '500242', '酉阳土家族苗族自治县', 3, '023', 108.767201, 28.839828, 1437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1447, '500231', '垫江县', 3, '023', 107.348692, 30.330012, 1437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1448, '500233', '忠县', 3, '023', 108.037518, 30.291537, 1437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1449, '500240', '石柱土家族自治县', 3, '023', 108.112448, 29.99853, 1437);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1450, '500100', '重庆城区', 2, '023', 106.504962, 29.533155, 1436);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1451, '500117', '合川区', 3, '023', 106.265554, 29.990993, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1452, '500152', '潼南区', 3, '023', 105.841818, 30.189554, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1453, '500151', '铜梁区', 3, '023', 106.054948, 29.839944, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1454, '500115', '长寿区', 3, '023', 107.074854, 29.833671, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1455, '500120', '璧山区', 3, '023', 106.231126, 29.593581, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1456, '500111', '大足区', 3, '023', 105.715319, 29.700498, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1457, '500153', '荣昌区', 3, '023', 105.594061, 29.403627, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1458, '500118', '永川区', 3, '023', 105.894714, 29.348748, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1459, '500103', '渝中区', 3, '023', 106.56288, 29.556742, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1460, '500156', '武隆区', 3, '023', 107.75655, 29.32376, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1461, '500119', '南川区', 3, '023', 107.098153, 29.156646, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1462, '500107', '九龙坡区', 3, '023', 106.480989, 29.523492, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1463, '500104', '大渡口区', 3, '023', 106.48613, 29.481002, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1464, '500101', '万州区', 3, '023', 108.380246, 30.807807, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1465, '500102', '涪陵区', 3, '023', 107.394905, 29.703652, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1466, '500110', '綦江区', 3, '023', 106.651417, 29.028091, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1467, '500155', '梁平区', 3, '023', 107.800034, 30.672168, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1468, '500154', '开州区', 3, '023', 108.413317, 31.167735, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1469, '500116', '江津区', 3, '023', 106.253156, 29.283387, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1470, '500114', '黔江区', 3, '023', 108.782577, 29.527548, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1471, '500108', '南岸区', 3, '023', 106.560813, 29.523992, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1472, '500113', '巴南区', 3, '023', 106.519423, 29.381919, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1473, '500106', '沙坪坝区', 3, '023', 106.4542, 29.541224, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1474, '500109', '北碚区', 3, '023', 106.437868, 29.82543, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1475, '500112', '渝北区', 3, '023', 106.512851, 29.601451, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1476, '500105', '江北区', 3, '023', 106.532844, 29.575352, 1450);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1477, '540000', '西藏自治区', 1, '', 91.132212, 29.660361, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1478, '540200', '日喀则市', 2, '0892', 88.885148, 29.267519, 1477);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1479, '540232', '仲巴县', 3, '0892', 84.032826, 29.768336, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1480, '540226', '昂仁县', 3, '0892', 87.23578, 29.294758, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1481, '540227', '谢通门县', 3, '0892', 88.260517, 29.431597, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1482, '540236', '萨嘎县', 3, '0892', 85.234622, 29.328194, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1483, '540221', '南木林县', 3, '0892', 89.099434, 29.680459, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1484, '540202', '桑珠孜区', 3, '0892', 88.88667, 29.267003, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1485, '540225', '拉孜县', 3, '0892', 87.63743, 29.085136, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1486, '540229', '仁布县', 3, '0892', 89.843207, 29.230299, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1487, '540224', '萨迦县', 3, '0892', 88.023007, 28.901077, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1488, '540234', '吉隆县', 3, '0892', 85.298349, 28.852416, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1489, '540228', '白朗县', 3, '0892', 89.263618, 29.106627, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1490, '540222', '江孜县', 3, '0892', 89.605044, 28.908845, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1491, '540223', '定日县', 3, '0892', 87.123887, 28.656667, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1492, '540235', '聂拉木县', 3, '0892', 85.981953, 28.15595, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1493, '540233', '亚东县', 3, '0892', 88.906806, 27.482772, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1494, '540237', '岗巴县', 3, '0892', 88.518903, 28.274371, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1495, '540230', '康马县', 3, '0892', 89.683406, 28.554719, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1496, '540231', '定结县', 3, '0892', 87.767723, 28.36409, 1478);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1497, '540500', '山南市', 2, '0893', 91.766529, 29.236023, 1477);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1498, '540523', '桑日县', 3, '0893', 92.015732, 29.259774, 1497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1499, '540521', '扎囊县', 3, '0893', 91.338, 29.246476, 1497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1500, '540502', '乃东区', 3, '0893', 91.76525, 29.236106, 1497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1501, '540528', '加查县', 3, '0893', 92.591043, 29.140921, 1497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1502, '540524', '琼结县', 3, '0893', 91.683753, 29.025242, 1497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1503, '540522', '贡嘎县', 3, '0893', 90.985271, 29.289078, 1497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1504, '540531', '浪卡子县', 3, '0893', 90.398747, 28.96836, 1497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1505, '540525', '曲松县', 3, '0893', 92.201066, 29.063656, 1497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1506, '540526', '措美县', 3, '0893', 91.432347, 28.437353, 1497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1507, '540530', '错那县', 3, '0893', 91.960132, 27.991707, 1497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1508, '540529', '隆子县', 3, '0893', 92.463309, 28.408548, 1497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1509, '540527', '洛扎县', 3, '0893', 90.858243, 28.385765, 1497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1510, '540600', '那曲市', 2, '0896', 92.060214, 31.476004, 1477);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1511, '540624', '安多县', 3, '0896', 91.681879, 32.260299, 1510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1512, '540623', '聂荣县', 3, '0896', 92.303659, 32.107855, 1510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1513, '540628', '巴青县', 3, '0896', 94.054049, 31.918691, 1510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1514, '540625', '申扎县', 3, '0896', 88.709777, 30.929056, 1510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1515, '540627', '班戈县', 3, '0896', 90.011822, 31.394578, 1510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1516, '540622', '比如县', 3, '0896', 93.68044, 31.479917, 1510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1517, '540626', '索县', 3, '0896', 93.784964, 31.886173, 1510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1518, '540621', '嘉黎县', 3, '0896', 93.232907, 30.640846, 1510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1519, '540602', '色尼区', 3, '0896', 92.061862, 31.475756, 1510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1520, '540629', '尼玛县', 3, '0896', 87.236646, 31.784979, 1510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1521, '540630', '双湖县', 3, '0896', 88.838578, 33.18698, 1510);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1522, '540300', '昌都市', 2, '0895', 97.178452, 31.136875, 1477);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1523, '540324', '丁青县', 3, '0895', 95.597748, 31.410681, 1522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1524, '540302', '卡若区', 3, '0895', 97.178255, 31.137035, 1522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1525, '540321', '江达县', 3, '0895', 98.218351, 31.499534, 1522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1526, '540323', '类乌齐县', 3, '0895', 96.601259, 31.213048, 1522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1527, '540330', '边坝县', 3, '0895', 94.707504, 30.933849, 1522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1528, '540322', '贡觉县', 3, '0895', 98.271191, 30.859206, 1522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1529, '540329', '洛隆县', 3, '0895', 95.823418, 30.741947, 1522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1530, '540325', '察雅县', 3, '0895', 97.565701, 30.653038, 1522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1531, '540326', '八宿县', 3, '0895', 96.917893, 30.053408, 1522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1532, '540327', '左贡县', 3, '0895', 97.840532, 29.671335, 1522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1533, '540328', '芒康县', 3, '0895', 98.596444, 29.686615, 1522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1534, '540100', '拉萨市', 2, '0891', 91.132212, 29.660361, 1477);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1535, '540122', '当雄县', 3, '0891', 91.103551, 30.474819, 1534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1536, '540121', '林周县', 3, '0891', 91.261842, 29.895754, 1534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1537, '540123', '尼木县', 3, '0891', 90.165545, 29.431346, 1534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1538, '540104', '达孜区', 3, '0891', 91.350976, 29.670314, 1534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1539, '540127', '墨竹工卡县', 3, '0891', 91.731158, 29.834657, 1534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1540, '540103', '堆龙德庆区', 3, '0891', 91.002823, 29.647347, 1534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1541, '540124', '曲水县', 3, '0891', 90.738051, 29.349895, 1534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1542, '540102', '城关区', 3, '0891', 91.132911, 29.659472, 1534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1543, '540400', '林芝市', 2, '0894', 94.362348, 29.654693, 1477);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1544, '540424', '波密县', 3, '0894', 95.768151, 29.858771, 1543);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1545, '540421', '工布江达县', 3, '0894', 93.246515, 29.88447, 1543);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1546, '540423', '墨脱县', 3, '0894', 95.332245, 29.32573, 1543);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1547, '540402', '巴宜区', 3, '0894', 94.360987, 29.653732, 1543);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1548, '540422', '米林县', 3, '0894', 94.213679, 29.213811, 1543);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1549, '540425', '察隅县', 3, '0894', 97.465002, 28.660244, 1543);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1550, '540426', '朗县', 3, '0894', 93.073429, 29.0446, 1543);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1551, '542500', '阿里地区', 2, '0897', 80.105498, 32.503187, 1477);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1552, '542526', '改则县', 3, '0897', 84.062384, 32.302076, 1551);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1553, '542522', '札达县', 3, '0897', 79.803191, 31.478587, 1551);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1554, '542527', '措勤县', 3, '0897', 85.159254, 31.016774, 1551);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1555, '542521', '普兰县', 3, '0897', 81.177588, 30.291896, 1551);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1556, '542523', '噶尔县', 3, '0897', 80.105005, 32.503373, 1551);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1557, '542525', '革吉县', 3, '0897', 81.142896, 32.389192, 1551);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1558, '542524', '日土县', 3, '0897', 79.731937, 33.382454, 1551);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1559, '430000', '湖南省', 1, '', 112.982279, 28.19409, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1560, '430600', '岳阳市', 2, '0730', 113.132855, 29.37029, 1559);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1561, '430611', '君山区', 3, '0730', 113.004082, 29.438062, 1560);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1562, '430603', '云溪区', 3, '0730', 113.27387, 29.473395, 1560);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1563, '430623', '华容县', 3, '0730', 112.559369, 29.524107, 1560);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1564, '430602', '岳阳楼区', 3, '0730', 113.120751, 29.366784, 1560);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1565, '430681', '汨罗市', 3, '0730', 113.079419, 28.803149, 1560);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1566, '430624', '湘阴县', 3, '0730', 112.889748, 28.677498, 1560);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1567, '430682', '临湘市', 3, '0730', 113.450809, 29.471594, 1560);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1568, '430621', '岳阳县', 3, '0730', 113.116073, 29.144843, 1560);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1569, '430626', '平江县', 3, '0730', 113.593751, 28.701523, 1560);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1570, '430900', '益阳市', 2, '0737', 112.355042, 28.570066, 1559);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1571, '430902', '资阳区', 3, '0737', 112.33084, 28.592771, 1570);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1572, '430922', '桃江县', 3, '0737', 112.139732, 28.520993, 1570);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1573, '430903', '赫山区', 3, '0737', 112.360946, 28.568327, 1570);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1574, '430923', '安化县', 3, '0737', 111.221824, 28.377421, 1570);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1575, '430981', '沅江市', 3, '0737', 112.361088, 28.839713, 1570);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1576, '430921', '南县', 3, '0737', 112.410399, 29.372181, 1570);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1577, '430400', '衡阳市', 2, '0734', 112.607693, 26.900358, 1559);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1578, '430423', '衡山县', 3, '0734', 112.86971, 27.234808, 1577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1579, '430424', '衡东县', 3, '0734', 112.950412, 27.083531, 1577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1580, '430421', '衡阳县', 3, '0734', 112.379643, 26.962388, 1577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1581, '430412', '南岳区', 3, '0734', 112.734147, 27.240536, 1577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1582, '430481', '耒阳市', 3, '0734', 112.847215, 26.414162, 1577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1583, '430406', '雁峰区', 3, '0734', 112.612241, 26.893694, 1577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1584, '430405', '珠晖区', 3, '0734', 112.626324, 26.891063, 1577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1585, '430407', '石鼓区', 3, '0734', 112.607635, 26.903908, 1577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1586, '430422', '衡南县', 3, '0734', 112.677459, 26.739973, 1577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1587, '430426', '祁东县', 3, '0734', 112.111192, 26.787109, 1577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1588, '430408', '蒸湘区', 3, '0734', 112.570608, 26.89087, 1577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1589, '430482', '常宁市', 3, '0734', 112.396821, 26.406773, 1577);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1590, '431300', '娄底市', 2, '0738', 112.008497, 27.728136, 1559);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1591, '431381', '冷水江市', 3, '0738', 111.434674, 27.685759, 1590);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1592, '431322', '新化县', 3, '0738', 111.306747, 27.737456, 1590);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1593, '431382', '涟源市', 3, '0738', 111.670847, 27.692301, 1590);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1594, '431321', '双峰县', 3, '0738', 112.198245, 27.459126, 1590);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1595, '431302', '娄星区', 3, '0738', 112.008486, 27.726643, 1590);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1596, '430700', '常德市', 2, '0736', 111.691347, 29.040225, 1559);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1597, '430726', '石门县', 3, '0736', 111.379087, 29.584703, 1596);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1598, '430723', '澧县', 3, '0736', 111.761682, 29.64264, 1596);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1599, '430781', '津市市', 3, '0736', 111.879609, 29.630867, 1596);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1600, '430724', '临澧县', 3, '0736', 111.645602, 29.443217, 1596);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1601, '430722', '汉寿县', 3, '0736', 111.968506, 28.907319, 1596);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1602, '430703', '鼎城区', 3, '0736', 111.685327, 29.014426, 1596);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1603, '430702', '武陵区', 3, '0736', 111.690718, 29.040477, 1596);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1604, '430725', '桃源县', 3, '0736', 111.484503, 28.902734, 1596);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1605, '430721', '安乡县', 3, '0736', 112.172289, 29.414483, 1596);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1606, '430800', '张家界市', 2, '0744', 110.479921, 29.127401, 1559);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1607, '430811', '武陵源区', 3, '0744', 110.54758, 29.347827, 1606);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1608, '430821', '慈利县', 3, '0744', 111.132702, 29.423876, 1606);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1609, '430822', '桑植县', 3, '0744', 110.164039, 29.399939, 1606);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1610, '430802', '永定区', 3, '0744', 110.484559, 29.125961, 1606);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1611, '431200', '怀化市', 2, '0745', 109.97824, 27.550082, 1559);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1612, '431281', '洪江市', 3, '0745', 109.831765, 27.201876, 1611);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1613, '431228', '芷江侗族自治县', 3, '0745', 109.687777, 27.437996, 1611);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1614, '431230', '通道侗族自治县', 3, '0745', 109.783359, 26.158349, 1611);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1615, '431223', '辰溪县', 3, '0745', 110.196953, 28.005474, 1611);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1616, '431222', '沅陵县', 3, '0745', 110.399161, 28.455554, 1611);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1617, '431224', '溆浦县', 3, '0745', 110.593373, 27.903802, 1611);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1618, '431229', '靖州苗族侗族自治县', 3, '0745', 109.691159, 26.573511, 1611);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1619, '431225', '会同县', 3, '0745', 109.720785, 26.870789, 1611);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1620, '431227', '新晃侗族自治县', 3, '0745', 109.174443, 27.359897, 1611);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1621, '431226', '麻阳苗族自治县', 3, '0745', 109.802807, 27.865991, 1611);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1622, '431221', '中方县', 3, '0745', 109.948061, 27.43736, 1611);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1623, '431202', '鹤城区', 3, '0745', 109.982242, 27.548474, 1611);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1624, '433100', '湘西土家族苗族自治州', 2, '0743', 109.739735, 28.314296, 1559);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1625, '433127', '永顺县', 3, '0743', 109.853292, 28.998068, 1624);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1626, '433125', '保靖县', 3, '0743', 109.651445, 28.709605, 1624);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1627, '433124', '花垣县', 3, '0743', 109.479063, 28.581352, 1624);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1628, '433101', '吉首市', 3, '0743', 109.738273, 28.314827, 1624);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1629, '433126', '古丈县', 3, '0743', 109.949592, 28.616973, 1624);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1630, '433122', '泸溪县', 3, '0743', 110.214428, 28.214516, 1624);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1631, '433130', '龙山县', 3, '0743', 109.441189, 29.453438, 1624);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1632, '433123', '凤凰县', 3, '0743', 109.599191, 27.948308, 1624);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1633, '430100', '长沙市', 2, '0731', 112.982279, 28.19409, 1559);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1634, '430105', '开福区', 3, '0731', 112.985525, 28.201336, 1633);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1635, '430104', '岳麓区', 3, '0731', 112.911591, 28.213044, 1633);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1636, '430103', '天心区', 3, '0731', 112.97307, 28.192375, 1633);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1637, '430181', '浏阳市', 3, '0731', 113.633301, 28.141112, 1633);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1638, '430121', '长沙县', 3, '0731', 113.080098, 28.237888, 1633);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1639, '430112', '望城区', 3, '0731', 112.819549, 28.347458, 1633);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1640, '430182', '宁乡市', 3, '0731', 112.553182, 28.253928, 1633);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1641, '430102', '芙蓉区', 3, '0731', 112.988094, 28.193106, 1633);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1642, '430111', '雨花区', 3, '0731', 113.016337, 28.109937, 1633);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1643, '430300', '湘潭市', 2, '0732', 112.944052, 27.82973, 1559);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1644, '430321', '湘潭县', 3, '0732', 112.952829, 27.778601, 1643);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1645, '430302', '雨湖区', 3, '0732', 112.907427, 27.86077, 1643);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1646, '430304', '岳塘区', 3, '0732', 112.927707, 27.828854, 1643);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1647, '430382', '韶山市', 3, '0732', 112.52848, 27.922682, 1643);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1648, '430381', '湘乡市', 3, '0732', 112.525217, 27.734918, 1643);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1649, '430200', '株洲市', 2, '0733', 113.151737, 27.835806, 1559);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1650, '430202', '荷塘区', 3, '0733', 113.162548, 27.833036, 1649);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1651, '430223', '攸县', 3, '0733', 113.345774, 27.000071, 1649);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1652, '430224', '茶陵县', 3, '0733', 113.546509, 26.789534, 1649);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1653, '430225', '炎陵县', 3, '0733', 113.776884, 26.489459, 1649);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1654, '430212', '渌口区', 3, '0733', 113.146175, 27.705844, 1649);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1655, '430203', '芦淞区', 3, '0733', 113.155169, 27.827246, 1649);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1656, '430211', '天元区', 3, '0733', 113.136252, 27.826909, 1649);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1657, '430204', '石峰区', 3, '0733', 113.11295, 27.871945, 1649);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1658, '430281', '醴陵市', 3, '0733', 113.507157, 27.657873, 1649);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1659, '430500', '邵阳市', 2, '0739', 111.46923, 27.237842, 1559);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1660, '430524', '隆回县', 3, '0739', 111.038785, 27.116002, 1659);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1661, '430503', '大祥区', 3, '0739', 111.462968, 27.233593, 1659);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1662, '430527', '绥宁县', 3, '0739', 110.155075, 26.580622, 1659);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1663, '430511', '北塔区', 3, '0739', 111.452315, 27.245688, 1659);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1664, '430502', '双清区', 3, '0739', 111.479756, 27.240001, 1659);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1665, '430523', '邵阳县', 3, '0739', 111.2757, 26.989713, 1659);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1666, '430529', '城步苗族自治县', 3, '0739', 110.313226, 26.363575, 1659);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1667, '430582', '邵东市', 3, '0739', 111.743168, 27.257273, 1659);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1668, '430522', '新邵县', 3, '0739', 111.459762, 27.311429, 1659);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1669, '430528', '新宁县', 3, '0739', 110.859115, 26.438912, 1659);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1670, '430581', '武冈市', 3, '0739', 110.636804, 26.732086, 1659);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1671, '430525', '洞口县', 3, '0739', 110.579212, 27.062286, 1659);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1672, '431000', '郴州市', 2, '0735', 113.032067, 25.793589, 1559);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1673, '431028', '安仁县', 3, '0735', 113.27217, 26.708625, 1672);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1674, '431026', '汝城县', 3, '0735', 113.685686, 25.553759, 1672);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1675, '431023', '永兴县', 3, '0735', 113.114819, 26.129392, 1672);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1676, '431003', '苏仙区', 3, '0735', 113.038698, 25.793157, 1672);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1677, '431081', '资兴市', 3, '0735', 113.23682, 25.974152, 1672);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1678, '431002', '北湖区', 3, '0735', 113.032208, 25.792628, 1672);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1679, '431027', '桂东县', 3, '0735', 113.945879, 26.073917, 1672);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1680, '431025', '临武县', 3, '0735', 112.564589, 25.279119, 1672);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1681, '431022', '宜章县', 3, '0735', 112.947884, 25.394345, 1672);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1682, '431024', '嘉禾县', 3, '0735', 112.370618, 25.587309, 1672);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1683, '431021', '桂阳县', 3, '0735', 112.734466, 25.737447, 1672);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1684, '431100', '永州市', 2, '0746', 111.608019, 26.434516, 1559);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1685, '431103', '冷水滩区', 3, '0746', 111.607156, 26.434364, 1684);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1686, '431121', '祁阳县', 3, '0746', 111.85734, 26.585929, 1684);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1687, '431122', '东安县', 3, '0746', 111.313035, 26.397278, 1684);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1688, '431102', '零陵区', 3, '0746', 111.626348, 26.223347, 1684);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1689, '431128', '新田县', 3, '0746', 112.220341, 25.906927, 1684);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1690, '431126', '宁远县', 3, '0746', 111.944529, 25.584112, 1684);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1691, '431123', '双牌县', 3, '0746', 111.662146, 25.959397, 1684);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1692, '431127', '蓝山县', 3, '0746', 112.194195, 25.375255, 1684);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1693, '431124', '道县', 3, '0746', 111.591614, 25.518444, 1684);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1694, '431125', '江永县', 3, '0746', 111.346803, 25.268154, 1684);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1695, '431129', '江华瑶族自治县', 3, '0746', 111.577276, 25.182596, 1684);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1696, '460000', '海南省', 1, '', 110.33119, 20.031971, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1697, '469007', '东方市', 2, '0807', 108.653789, 19.10198, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1698, '469022', '屯昌县', 2, '1892', 110.102773, 19.362916, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1699, '469006', '万宁市', 2, '1898', 110.388793, 18.796216, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1700, '469024', '临高县', 2, '1896', 109.687697, 19.908293, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1701, '469026', '昌江黎族自治县', 2, '0803', 109.053351, 19.260968, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1702, '469021', '定安县', 2, '0806', 110.349235, 19.684966, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1703, '469025', '白沙黎族自治县', 2, '0802', 109.452606, 19.224584, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1704, '469002', '琼海市', 2, '1894', 110.466785, 19.246011, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1705, '469030', '琼中黎族苗族自治县', 2, '1899', 109.839996, 19.03557, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1706, '460400', '儋州市', 2, '0805', 109.576782, 19.517486, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1707, '469027', '乐东黎族自治县', 2, '2802', 109.175444, 18.74758, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1708, '469023', '澄迈县', 2, '0804', 110.007147, 19.737095, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1709, '460200', '三亚市', 2, '0899', 109.508268, 18.247872, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1710, '460204', '天涯区', 3, '0899', 109.506357, 18.24734, 1709);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1711, '460202', '海棠区', 3, '0899', 109.760778, 18.407516, 1709);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1712, '460203', '吉阳区', 3, '0899', 109.512081, 18.247436, 1709);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1713, '460205', '崖州区', 3, '0899', 109.174306, 18.352192, 1709);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1714, '469028', '陵水黎族自治县', 2, '0809', 110.037218, 18.505006, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1715, '469005', '文昌市', 2, '1893', 110.753975, 19.612986, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1716, '460300', '三沙市', 2, '2898', 112.34882, 16.831039, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1717, '460301', '西沙区', 3, '2898', 112.3386402, 16.8310066, 1716);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1718, '460302', '南沙区', 3, '2898', 112.891018, 9.543575, 1716);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1719, '460100', '海口市', 2, '0898', 110.33119, 20.031971, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1720, '460108', '美兰区', 3, '0898', 110.356566, 20.03074, 1719);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1721, '460106', '龙华区', 3, '0898', 110.330373, 20.031026, 1719);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1722, '460107', '琼山区', 3, '0898', 110.354722, 20.001051, 1719);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1723, '460105', '秀英区', 3, '0898', 110.282393, 20.008145, 1719);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1724, '469029', '保亭黎族苗族自治县', 2, '0801', 109.70245, 18.636371, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1725, '469001', '五指山市', 2, '1897', 109.516662, 18.776921, 1696);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1726, '630000', '青海省', 1, '', 101.778916, 36.623178, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1727, '632600', '果洛藏族自治州', 2, '0975', 100.242143, 34.4736, 1726);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1728, '632623', '甘德县', 3, '0975', 99.902589, 33.966987, 1727);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1729, '632621', '玛沁县', 3, '0975', 100.243531, 34.473386, 1727);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1730, '632624', '达日县', 3, '0975', 99.651715, 33.753259, 1727);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1731, '632625', '久治县', 3, '0975', 101.484884, 33.430217, 1727);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1732, '632622', '班玛县', 3, '0975', 100.737955, 32.931589, 1727);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1733, '632626', '玛多县', 3, '0975', 98.211343, 34.91528, 1727);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1734, '632500', '海南藏族自治州', 2, '0974', 100.619542, 36.280353, 1726);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1735, '632523', '贵德县', 3, '0974', 101.431856, 36.040456, 1734);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1736, '632521', '共和县', 3, '0974', 100.619597, 36.280286, 1734);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1737, '632525', '贵南县', 3, '0974', 100.74792, 35.587085, 1734);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1738, '632522', '同德县', 3, '0974', 100.579465, 35.254492, 1734);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1739, '632524', '兴海县', 3, '0974', 99.986963, 35.58909, 1734);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1740, '632700', '玉树藏族自治州', 2, '0976', 97.008522, 33.004049, 1726);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1741, '632723', '称多县', 3, '0976', 97.110893, 33.367884, 1740);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1742, '632724', '治多县', 3, '0976', 95.616843, 33.852322, 1740);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1743, '632726', '曲麻莱县', 3, '0976', 95.800674, 34.12654, 1740);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1744, '632701', '玉树市', 3, '0976', 97.008762, 33.00393, 1740);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1745, '632722', '杂多县', 3, '0976', 95.293423, 32.891886, 1740);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1746, '632725', '囊谦县', 3, '0976', 96.479797, 32.203206, 1740);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1747, '632300', '黄南藏族自治州', 2, '0973', 102.019988, 35.517744, 1726);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1748, '632322', '尖扎县', 3, '0973', 102.031953, 35.938205, 1747);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1749, '632321', '同仁县', 3, '0973', 102.017604, 35.516337, 1747);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1750, '632324', '河南蒙古族自治县', 3, '0973', 101.611877, 34.734522, 1747);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1751, '632323', '泽库县', 3, '0973', 101.469343, 35.036842, 1747);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1752, '632800', '海西蒙古族藏族自治州', 2, '0977', 97.370785, 37.374663, 1726);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1753, '632823', '天峻县', 3, '0977', 99.02078, 37.29906, 1752);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1754, '632802', '德令哈市', 3, '0977', 97.370143, 37.374555, 1752);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1755, '632822', '都兰县', 3, '0977', 98.089161, 36.298553, 1752);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1756, '632801', '格尔木市', 3, '0977', 94.905777, 36.401541, 1752);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1757, '632825', '海西蒙古族藏族自治州直辖', 3, '0977', 95.357233, 37.853631, 1752);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1758, '632821', '乌兰县', 3, '0977', 98.479852, 36.930389, 1752);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1759, '632803', '茫崖市', 3, '0977', 90.855955, 38.247117, 1752);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1760, '630200', '海东市', 2, '0972', 102.10327, 36.502916, 1726);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1761, '630224', '化隆回族自治县', 3, '0972', 102.262329, 36.098322, 1760);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1762, '630222', '民和回族土族自治县', 3, '0972', 102.804209, 36.329451, 1760);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1763, '630202', '乐都区', 3, '0972', 102.402431, 36.480291, 1760);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1764, '630225', '循化撒拉族自治县', 3, '0972', 102.486534, 35.847247, 1760);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1765, '630203', '平安区', 3, '0972', 102.104295, 36.502714, 1760);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1766, '630223', '互助土族自治县', 3, '0972', 101.956734, 36.83994, 1760);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1767, '630100', '西宁市', 2, '0971', 101.778916, 36.623178, 1726);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1768, '630123', '湟源县', 3, '0971', 101.263435, 36.684818, 1767);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1769, '630102', '城东区', 3, '0971', 101.796095, 36.616043, 1767);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1770, '630121', '大通回族土族自治县', 3, '0971', 101.684183, 36.931343, 1767);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1771, '630104', '城西区', 3, '0971', 101.763649, 36.628323, 1767);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1772, '630106', '湟中区', 3, '0971', 101.569475, 36.500419, 1767);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1773, '630103', '城中区', 3, '0971', 101.784554, 36.621181, 1767);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1774, '630105', '城北区', 3, '0971', 101.761297, 36.648448, 1767);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1775, '632200', '海北藏族自治州', 2, '0970', 100.901059, 36.959435, 1726);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1776, '632223', '海晏县', 3, '0970', 100.90049, 36.959542, 1775);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1777, '632221', '门源回族自治县', 3, '0970', 101.618461, 37.376627, 1775);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1778, '632224', '刚察县', 3, '0970', 100.138417, 37.326263, 1775);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1779, '632222', '祁连县', 3, '0970', 100.249778, 38.175409, 1775);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1780, '350000', '福建省', 1, '', 119.306239, 26.075302, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1781, '350900', '宁德市', 2, '0593', 119.527082, 26.65924, 1780);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1782, '350924', '寿宁县', 3, '0593', 119.506733, 27.457798, 1781);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1783, '350923', '屏南县', 3, '0593', 118.987544, 26.910826, 1781);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1784, '350982', '福鼎市', 3, '0593', 120.219761, 27.318884, 1781);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1785, '350981', '福安市', 3, '0593', 119.650798, 27.084246, 1781);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1786, '350922', '古田县', 3, '0593', 118.743156, 26.577491, 1781);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1787, '350925', '周宁县', 3, '0593', 119.338239, 27.103106, 1781);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1788, '350902', '蕉城区', 3, '0593', 119.527225, 26.659253, 1781);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1789, '350921', '霞浦县', 3, '0593', 120.005214, 26.882068, 1781);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1790, '350926', '柘荣县', 3, '0593', 119.898226, 27.236163, 1781);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1791, '350700', '南平市', 2, '0599', 118.178459, 26.635627, 1780);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1792, '350722', '浦城县', 3, '0599', 118.536822, 27.920412, 1791);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1793, '350782', '武夷山市', 3, '0599', 118.032796, 27.751733, 1791);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1794, '350703', '建阳区', 3, '0599', 118.12267, 27.332067, 1791);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1795, '350723', '光泽县', 3, '0599', 117.337897, 27.542803, 1791);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1796, '350724', '松溪县', 3, '0599', 118.783491, 27.525785, 1791);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1797, '350783', '建瓯市', 3, '0599', 118.321765, 27.03502, 1791);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1798, '350721', '顺昌县', 3, '0599', 117.80771, 26.792851, 1791);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1799, '350781', '邵武市', 3, '0599', 117.491544, 27.337952, 1791);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1800, '350725', '政和县', 3, '0599', 118.858661, 27.365398, 1791);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1801, '350702', '延平区', 3, '0599', 118.178918, 26.636079, 1791);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1802, '350300', '莆田市', 2, '0594', 119.007558, 25.431011, 1780);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1803, '350322', '仙游县', 3, '0594', 118.694331, 25.356529, 1802);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1804, '350304', '荔城区', 3, '0594', 119.020047, 25.430047, 1802);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1805, '350305', '秀屿区', 3, '0594', 119.092607, 25.316141, 1802);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1806, '350302', '城厢区', 3, '0594', 119.001028, 25.433737, 1802);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1807, '350303', '涵江区', 3, '0594', 119.119102, 25.459273, 1802);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1808, '350800', '龙岩市', 2, '0597', 117.02978, 25.091603, 1780);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1809, '350881', '漳平市', 3, '0597', 117.42073, 25.291597, 1808);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1810, '350823', '上杭县', 3, '0597', 116.424774, 25.050019, 1808);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1811, '350824', '武平县', 3, '0597', 116.100928, 25.08865, 1808);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1812, '350803', '永定区', 3, '0597', 116.732691, 24.720442, 1808);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1813, '350802', '新罗区', 3, '0597', 117.030721, 25.0918, 1808);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1814, '350821', '长汀县', 3, '0597', 116.361007, 25.842278, 1808);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1815, '350825', '连城县', 3, '0597', 116.756687, 25.708506, 1808);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1816, '350100', '福州市', 2, '0591', 119.306239, 26.075302, 1780);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1817, '350122', '连江县', 3, '0591', 119.538365, 26.202109, 1816);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1818, '350123', '罗源县', 3, '0591', 119.552645, 26.487234, 1816);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1819, '350124', '闽清县', 3, '0591', 118.868416, 26.223793, 1816);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1820, '350121', '闽侯县', 3, '0591', 119.145117, 26.148567, 1816);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1821, '350104', '仓山区', 3, '0591', 119.320988, 26.038912, 1816);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1822, '350125', '永泰县', 3, '0591', 118.939089, 25.864825, 1816);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1823, '350181', '福清市', 3, '0591', 119.376992, 25.720402, 1816);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1824, '350112', '长乐区', 3, '0591', 119.510849, 25.960583, 1816);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1825, '350128', '平潭县', 3, '0591', 119.791197, 25.503672, 1816);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1826, '350103', '台江区', 3, '0591', 119.310156, 26.058616, 1816);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1827, '350102', '鼓楼区', 3, '0591', 119.29929, 26.082284, 1816);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1828, '350105', '马尾区', 3, '0591', 119.458725, 25.991975, 1816);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1829, '350111', '晋安区', 3, '0591', 119.328597, 26.078837, 1816);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1830, '350400', '三明市', 2, '0598', 117.635001, 26.265444, 1780);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1831, '350429', '泰宁县', 3, '0598', 117.177522, 26.897995, 1830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1832, '350402', '梅列区', 3, '0598', 117.63687, 26.269208, 1830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1833, '350424', '宁化县', 3, '0598', 116.659725, 26.259932, 1830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1834, '350423', '清流县', 3, '0598', 116.815821, 26.17761, 1830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1835, '350481', '永安市', 3, '0598', 117.364447, 25.974075, 1830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1836, '350403', '三元区', 3, '0598', 117.607418, 26.234191, 1830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1837, '350425', '大田县', 3, '0598', 117.849355, 25.690803, 1830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1838, '350426', '尤溪县', 3, '0598', 118.188577, 26.169261, 1830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1839, '350430', '建宁县', 3, '0598', 116.845832, 26.831398, 1830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1840, '350427', '沙县', 3, '0598', 117.789095, 26.397361, 1830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1841, '350421', '明溪县', 3, '0598', 117.201845, 26.357375, 1830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1842, '350428', '将乐县', 3, '0598', 117.473558, 26.728667, 1830);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1843, '350500', '泉州市', 2, '0595', 118.589421, 24.908853, 1780);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1844, '350526', '德化县', 3, '0595', 118.242986, 25.489004, 1843);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1845, '350525', '永春县', 3, '0595', 118.29503, 25.320721, 1843);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1846, '350504', '洛江区', 3, '0595', 118.670312, 24.941153, 1843);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1847, '350505', '泉港区', 3, '0595', 118.912285, 25.126859, 1843);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1848, '350503', '丰泽区', 3, '0595', 118.605147, 24.896041, 1843);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1849, '350581', '石狮市', 3, '0595', 118.628402, 24.731978, 1843);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1850, '350583', '南安市', 3, '0595', 118.387031, 24.959494, 1843);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1851, '350521', '惠安县', 3, '0595', 118.798954, 25.028718, 1843);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1852, '350582', '晋江市', 3, '0595', 118.577338, 24.807322, 1843);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1853, '350527', '金门县', 3, '0595', 118.323221, 24.436417, 1843);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1854, '350502', '鲤城区', 3, '0595', 118.588929, 24.907645, 1843);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1855, '350524', '安溪县', 3, '0595', 118.186014, 25.056824, 1843);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1856, '350200', '厦门市', 2, '0592', 118.11022, 24.490474, 1780);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1857, '350206', '湖里区', 3, '0592', 118.10943, 24.512764, 1856);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1858, '350203', '思明区', 3, '0592', 118.087828, 24.462059, 1856);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1859, '350213', '翔安区', 3, '0592', 118.242811, 24.637479, 1856);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1860, '350205', '海沧区', 3, '0592', 118.036364, 24.492512, 1856);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1861, '350211', '集美区', 3, '0592', 118.100869, 24.572874, 1856);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1862, '350212', '同安区', 3, '0592', 118.150455, 24.729333, 1856);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1863, '350600', '漳州市', 2, '0596', 117.661801, 24.510897, 1780);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1864, '350629', '华安县', 3, '0596', 117.53631, 25.001416, 1863);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1865, '350627', '南靖县', 3, '0596', 117.365462, 24.516425, 1863);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1866, '350681', '龙海市', 3, '0596', 117.817292, 24.445341, 1863);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1867, '350623', '漳浦县', 3, '0596', 117.614023, 24.117907, 1863);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1868, '350626', '东山县', 3, '0596', 117.427679, 23.702845, 1863);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1869, '350624', '诏安县', 3, '0596', 117.176083, 23.710834, 1863);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1870, '350602', '芗城区', 3, '0596', 117.656461, 24.509955, 1863);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1871, '350603', '龙文区', 3, '0596', 117.671387, 24.515656, 1863);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1872, '350622', '云霄县', 3, '0596', 117.340946, 23.950486, 1863);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1873, '350628', '平和县', 3, '0596', 117.313549, 24.366158, 1863);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1874, '350625', '长泰县', 3, '0596', 117.755913, 24.621475, 1863);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1875, '640000', '宁夏回族自治区', 1, '', 106.278179, 38.46637, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1876, '640200', '石嘴山市', 2, '0952', 106.376173, 39.01333, 1875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1877, '640202', '大武口区', 3, '0952', 106.376651, 39.014158, 1876);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1878, '640205', '惠农区', 3, '0952', 106.775513, 39.230094, 1876);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1879, '640221', '平罗县', 3, '0952', 106.54489, 38.90674, 1876);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1880, '640500', '中卫市', 2, '1953', 105.189568, 37.514951, 1875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1881, '640521', '中宁县', 3, '1953', 105.675784, 37.489736, 1880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1882, '640522', '海原县', 3, '1953', 105.647323, 36.562007, 1880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1883, '640502', '沙坡头区', 3, '1953', 105.190536, 37.514564, 1880);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1884, '640400', '固原市', 2, '0954', 106.285241, 36.004561, 1875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1885, '640423', '隆德县', 3, '0954', 106.12344, 35.618234, 1884);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1886, '640425', '彭阳县', 3, '0954', 106.641512, 35.849975, 1884);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1887, '640402', '原州区', 3, '0954', 106.28477, 36.005337, 1884);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1888, '640424', '泾源县', 3, '0954', 106.338674, 35.49344, 1884);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1889, '640422', '西吉县', 3, '0954', 105.731801, 35.965384, 1884);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1890, '640100', '银川市', 2, '0951', 106.278179, 38.46637, 1875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1891, '640105', '西夏区', 3, '0951', 106.132116, 38.492424, 1890);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1892, '640121', '永宁县', 3, '0951', 106.253781, 38.28043, 1890);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1893, '640106', '金凤区', 3, '0951', 106.228486, 38.477353, 1890);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1894, '640122', '贺兰县', 3, '0951', 106.345904, 38.554563, 1890);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1895, '640104', '兴庆区', 3, '0951', 106.278393, 38.46747, 1890);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1896, '640181', '灵武市', 3, '0951', 106.334701, 38.094058, 1890);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1897, '640300', '吴忠市', 2, '0953', 106.199409, 37.986165, 1875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1898, '640381', '青铜峡市', 3, '0953', 106.075395, 38.021509, 1897);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1899, '640324', '同心县', 3, '0953', 105.914764, 36.9829, 1897);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1900, '640323', '盐池县', 3, '0953', 107.40541, 37.784222, 1897);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1901, '640303', '红寺堡区', 3, '0953', 106.067315, 37.421616, 1897);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1902, '640302', '利通区', 3, '0953', 106.199419, 37.985967, 1897);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1903, '450000', '广西壮族自治区', 1, '', 108.320004, 22.82402, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1904, '451000', '百色市', 2, '0776', 106.616285, 23.897742, 1903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1905, '451028', '乐业县', 3, '0776', 106.559638, 24.782204, 1904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1906, '451031', '隆林各族自治县', 3, '0776', 105.342363, 24.774318, 1904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1907, '451030', '西林县', 3, '0776', 105.095025, 24.492041, 1904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1908, '451027', '凌云县', 3, '0776', 106.56487, 24.345643, 1904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1909, '451002', '右江区', 3, '0776', 106.615727, 23.897675, 1904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1910, '451082', '平果市', 3, '0776', 107.580403, 23.320479, 1904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1911, '451026', '那坡县', 3, '0776', 105.833553, 23.400785, 1904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1912, '451081', '靖西市', 3, '0776', 106.417549, 23.134766, 1904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1913, '451029', '田林县', 3, '0776', 106.235047, 24.290262, 1904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1914, '451003', '田阳区', 3, '0776', 106.904315, 23.736079, 1904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1915, '451024', '德保县', 3, '0776', 106.618164, 23.321464, 1904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1916, '451022', '田东县', 3, '0776', 107.12426, 23.600444, 1904);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1917, '450500', '北海市', 2, '0779', 109.119254, 21.473343, 1903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1918, '450512', '铁山港区', 3, '0779', 109.450573, 21.5928, 1917);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1919, '450503', '银海区', 3, '0779', 109.118707, 21.444909, 1917);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1920, '450502', '海城区', 3, '0779', 109.107529, 21.468443, 1917);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1921, '450521', '合浦县', 3, '0779', 109.200695, 21.663554, 1917);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1922, '450700', '钦州市', 2, '0777', 108.624175, 21.967127, 1903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1923, '450702', '钦南区', 3, '0777', 108.626629, 21.966808, 1922);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1924, '450703', '钦北区', 3, '0777', 108.44911, 22.132761, 1922);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1925, '450722', '浦北县', 3, '0777', 109.556341, 22.268335, 1922);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1926, '450721', '灵山县', 3, '0777', 109.293468, 22.418041, 1922);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1927, '450200', '柳州市', 2, '0772', 109.411703, 24.314617, 1903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1928, '450226', '三江侗族自治县', 3, '0772', 109.614846, 25.78553, 1927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1929, '450225', '融水苗族自治县', 3, '0772', 109.252744, 25.068812, 1927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1930, '450224', '融安县', 3, '0772', 109.403621, 25.214703, 1927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1931, '450223', '鹿寨县', 3, '0772', 109.740805, 24.483405, 1927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1932, '450205', '柳北区', 3, '0772', 109.406577, 24.359145, 1927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1933, '450202', '城中区', 3, '0772', 109.411749, 24.312324, 1927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1934, '450222', '柳城县', 3, '0772', 109.245812, 24.655121, 1927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1935, '450203', '鱼峰区', 3, '0772', 109.415364, 24.303848, 1927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1936, '450206', '柳江区', 3, '0772', 109.334503, 24.257512, 1927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1937, '450204', '柳南区', 3, '0772', 109.395936, 24.287013, 1927);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1938, '451300', '来宾市', 2, '1772', 109.229772, 23.733766, 1903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1939, '451322', '象州县', 3, '1772', 109.684555, 23.959824, 1938);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1940, '451321', '忻城县', 3, '1772', 108.667361, 24.064779, 1938);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1941, '451324', '金秀瑶族自治县', 3, '1772', 110.188556, 24.134941, 1938);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1942, '451302', '兴宾区', 3, '1772', 109.230541, 23.732926, 1938);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1943, '451323', '武宣县', 3, '1772', 109.66287, 23.604162, 1938);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1944, '451381', '合山市', 3, '1772', 108.88858, 23.81311, 1938);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1945, '450100', '南宁市', 2, '0771', 108.320004, 22.82402, 1903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1946, '450125', '上林县', 3, '0771', 108.603937, 23.431769, 1945);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1947, '450110', '武鸣区', 3, '0771', 108.280717, 23.157163, 1945);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1948, '450124', '马山县', 3, '0771', 108.172903, 23.711758, 1945);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1949, '450127', '横县', 3, '0771', 109.270987, 22.68743, 1945);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1950, '450126', '宾阳县', 3, '0771', 108.816735, 23.216884, 1945);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1951, '450108', '良庆区', 3, '0771', 108.322102, 22.75909, 1945);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1952, '450109', '邕宁区', 3, '0771', 108.484251, 22.756598, 1945);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1953, '450105', '江南区', 3, '0771', 108.310478, 22.799593, 1945);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1954, '450102', '兴宁区', 3, '0771', 108.320189, 22.819511, 1945);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1955, '450107', '西乡塘区', 3, '0771', 108.306903, 22.832779, 1945);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1956, '450103', '青秀区', 3, '0771', 108.346113, 22.816614, 1945);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1957, '450123', '隆安县', 3, '0771', 107.688661, 23.174763, 1945);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1958, '450400', '梧州市', 2, '0774', 111.297604, 23.474803, 1903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1959, '450423', '蒙山县', 3, '0774', 110.5226, 24.199829, 1958);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1960, '450405', '长洲区', 3, '0774', 111.275678, 23.4777, 1958);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1961, '450406', '龙圩区', 3, '0774', 111.246035, 23.40996, 1958);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1962, '450481', '岑溪市', 3, '0774', 110.998114, 22.918406, 1958);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1963, '450421', '苍梧县', 3, '0774', 111.544008, 23.845097, 1958);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1964, '450403', '万秀区', 3, '0774', 111.315817, 23.471318, 1958);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1965, '450422', '藤县', 3, '0774', 110.931826, 23.373963, 1958);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1966, '450300', '桂林市', 2, '0773', 110.299121, 25.274215, 1903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1967, '450325', '兴安县', 3, '0773', 110.670783, 25.609554, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1968, '450312', '临桂区', 3, '0773', 110.205487, 25.246257, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1969, '450326', '永福县', 3, '0773', 109.989208, 24.986692, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1970, '450332', '恭城瑶族自治县', 3, '0773', 110.82952, 24.833612, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1971, '450311', '雁山区', 3, '0773', 110.305667, 25.077646, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1972, '450305', '七星区', 3, '0773', 110.317577, 25.254339, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1973, '450321', '阳朔县', 3, '0773', 110.494699, 24.77534, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1974, '450381', '荔浦市', 3, '0773', 110.400149, 24.497786, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1975, '450330', '平乐县', 3, '0773', 110.642821, 24.632216, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1976, '450328', '龙胜各族自治县', 3, '0773', 110.009423, 25.796428, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1977, '450302', '秀峰区', 3, '0773', 110.292445, 25.278544, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1978, '450304', '象山区', 3, '0773', 110.284882, 25.261986, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1979, '450327', '灌阳县', 3, '0773', 111.160248, 25.489098, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1980, '450324', '全州县', 3, '0773', 111.072989, 25.929897, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1981, '450323', '灵川县', 3, '0773', 110.325712, 25.408541, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1982, '450303', '叠彩区', 3, '0773', 110.300783, 25.301334, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1983, '450329', '资源县', 3, '0773', 110.642587, 26.0342, 1966);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1984, '451400', '崇左市', 2, '1771', 107.353926, 22.404108, 1903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1985, '451425', '天等县', 3, '1771', 107.142441, 23.082484, 1984);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1986, '451402', '江州区', 3, '1771', 107.354443, 22.40469, 1984);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1987, '451421', '扶绥县', 3, '1771', 107.911533, 22.635821, 1984);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1988, '451424', '大新县', 3, '1771', 107.200803, 22.833369, 1984);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1989, '451481', '凭祥市', 3, '1771', 106.759038, 22.108882, 1984);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1990, '451423', '龙州县', 3, '1771', 106.857502, 22.343716, 1984);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1991, '451422', '宁明县', 3, '1771', 107.067616, 22.131353, 1984);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1992, '450600', '防城港市', 2, '0770', 108.345478, 21.614631, 1903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1993, '450602', '港口区', 3, '0770', 108.346281, 21.614406, 1992);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1994, '450603', '防城区', 3, '0770', 108.358426, 21.764758, 1992);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1995, '450681', '东兴市', 3, '0770', 107.97017, 21.541172, 1992);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1996, '450621', '上思县', 3, '0770', 107.982139, 22.151423, 1992);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1997, '451100', '贺州市', 2, '1774', 111.552056, 24.414141, 1903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1998, '451123', '富川瑶族自治县', 3, '1774', 111.277228, 24.81896, 1997);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (1999, '451121', '昭平县', 3, '1774', 110.810865, 24.172958, 1997);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2000, '451122', '钟山县', 3, '1774', 111.303629, 24.528566, 1997);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2001, '451103', '平桂区', 3, '1774', 111.524014, 24.417148, 1997);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2002, '451102', '八步区', 3, '1774', 111.551991, 24.412446, 1997);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2003, '450900', '玉林市', 2, '0775', 110.154393, 22.63136, 1903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2004, '450903', '福绵区', 3, '0775', 110.054155, 22.58163, 2003);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2005, '450924', '兴业县', 3, '0775', 109.877768, 22.74187, 2003);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2006, '450923', '博白县', 3, '0775', 109.980004, 22.271285, 2003);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2007, '450922', '陆川县', 3, '0775', 110.264842, 22.321054, 2003);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2008, '450921', '容县', 3, '0775', 110.552467, 22.856435, 2003);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2009, '450902', '玉州区', 3, '0775', 110.154912, 22.632132, 2003);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2010, '450981', '北流市', 3, '0775', 110.348052, 22.701648, 2003);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2011, '450800', '贵港市', 2, '1755', 109.602146, 23.0936, 1903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2012, '450804', '覃塘区', 3, '1755', 109.415697, 23.132815, 2011);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2013, '450802', '港北区', 3, '1755', 109.59481, 23.107677, 2011);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2014, '450803', '港南区', 3, '1755', 109.604665, 23.067516, 2011);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2015, '450881', '桂平市', 3, '1755', 110.074668, 23.382473, 2011);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2016, '450821', '平南县', 3, '1755', 110.397485, 23.544546, 2011);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2017, '451200', '河池市', 2, '0778', 108.062105, 24.695899, 1903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2018, '451225', '罗城仫佬族自治县', 3, '0778', 108.902453, 24.779327, 2017);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2019, '451221', '南丹县', 3, '0778', 107.546605, 24.983192, 2017);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2020, '451222', '天峨县', 3, '0778', 107.174939, 24.985964, 2017);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2021, '451226', '环江毛南族自治县', 3, '0778', 108.258669, 24.827628, 2017);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2022, '451203', '宜州区', 3, '0778', 108.653965, 24.492193, 2017);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2023, '451223', '凤山县', 3, '0778', 107.044592, 24.544561, 2017);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2024, '451202', '金城江区', 3, '0778', 108.062131, 24.695625, 2017);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2025, '451228', '都安瑶族自治县', 3, '0778', 108.102761, 23.934964, 2017);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2026, '451229', '大化瑶族自治县', 3, '0778', 107.9945, 23.739596, 2017);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2027, '451224', '东兰县', 3, '0778', 107.373696, 24.509367, 2017);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2028, '451227', '巴马瑶族自治县', 3, '0778', 107.253126, 24.139538, 2017);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2029, '320000', '江苏省', 1, '', 118.767413, 32.041544, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2030, '320700', '连云港市', 2, '0518', 119.178821, 34.600018, 2029);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2031, '320722', '东海县', 3, '0518', 118.766489, 34.522859, 2030);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2032, '320703', '连云区', 3, '0518', 119.366487, 34.739529, 2030);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2033, '320723', '灌云县', 3, '0518', 119.255741, 34.298436, 2030);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2034, '320724', '灌南县', 3, '0518', 119.352331, 34.092553, 2030);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2035, '320707', '赣榆区', 3, '0518', 119.128774, 34.839154, 2030);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2036, '320706', '海州区', 3, '0518', 119.179793, 34.601584, 2030);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2037, '321300', '宿迁市', 2, '0527', 118.275162, 33.963008, 2029);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2038, '321322', '沭阳县', 3, '0527', 118.775889, 34.129097, 2037);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2039, '321323', '泗阳县', 3, '0527', 118.681284, 33.711433, 2037);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2040, '321324', '泗洪县', 3, '0527', 118.211824, 33.456538, 2037);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2041, '321311', '宿豫区', 3, '0527', 118.330012, 33.941071, 2037);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2042, '321302', '宿城区', 3, '0527', 118.278984, 33.937726, 2037);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2043, '320100', '南京市', 2, '025', 118.767413, 32.041544, 2029);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2044, '320111', '浦口区', 3, '025', 118.625307, 32.05839, 2043);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2045, '320114', '雨花台区', 3, '025', 118.77207, 31.995946, 2043);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2046, '320115', '江宁区', 3, '025', 118.850621, 31.953418, 2043);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2047, '320117', '溧水区', 3, '025', 119.028732, 31.653061, 2043);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2048, '320116', '六合区', 3, '025', 118.85065, 32.340655, 2043);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2049, '320118', '高淳区', 3, '025', 118.87589, 31.327132, 2043);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2050, '320105', '建邺区', 3, '025', 118.732688, 32.004538, 2043);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2051, '320106', '鼓楼区', 3, '025', 118.769739, 32.066966, 2043);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2052, '320104', '秦淮区', 3, '025', 118.786088, 32.033818, 2043);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2053, '320113', '栖霞区', 3, '025', 118.808702, 32.102147, 2043);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2054, '320102', '玄武区', 3, '025', 118.792199, 32.050678, 2043);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2055, '320600', '南通市', 2, '0513', 120.864608, 32.016212, 2029);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2056, '320682', '如皋市', 3, '0513', 120.566324, 32.391591, 2055);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2057, '320684', '海门市', 3, '0513', 121.176609, 31.893528, 2055);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2058, '320685', '海安市', 3, '0513', 120.465995, 32.540288, 2055);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2059, '320623', '如东县', 3, '0513', 121.186088, 32.311832, 2055);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2060, '320681', '启东市', 3, '0513', 121.659724, 31.810158, 2055);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2061, '320611', '港闸区', 3, '0513', 120.8339, 32.040299, 2055);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2062, '320612', '通州区', 3, '0513', 121.073171, 32.084287, 2055);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2063, '320602', '崇川区', 3, '0513', 120.86635, 32.015278, 2055);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2064, '320800', '淮安市', 2, '0517', 119.021265, 33.597506, 2029);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2065, '320804', '淮阴区', 3, '0517', 119.020817, 33.622452, 2064);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2066, '320830', '盱眙县', 3, '0517', 118.493823, 33.00439, 2064);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2067, '320831', '金湖县', 3, '0517', 119.016936, 33.018162, 2064);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2068, '320813', '洪泽区', 3, '0517', 118.867875, 33.294975, 2064);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2069, '320826', '涟水县', 3, '0517', 119.266078, 33.771308, 2064);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2070, '320812', '清江浦区', 3, '0517', 119.019454, 33.603234, 2064);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2071, '320803', '淮安区', 3, '0517', 119.14634, 33.507499, 2064);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2072, '321000', '扬州市', 2, '0514', 119.421003, 32.393159, 2029);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2073, '321003', '邗江区', 3, '0514', 119.397777, 32.377899, 2072);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2074, '321002', '广陵区', 3, '0514', 119.442267, 32.392154, 2072);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2075, '321081', '仪征市', 3, '0514', 119.182443, 32.271965, 2072);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2076, '321084', '高邮市', 3, '0514', 119.443842, 32.785164, 2072);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2077, '321023', '宝应县', 3, '0514', 119.321284, 33.23694, 2072);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2078, '321012', '江都区', 3, '0514', 119.567481, 32.426564, 2072);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2079, '321200', '泰州市', 2, '0523', 119.915176, 32.484882, 2029);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2080, '321282', '靖江市', 3, '0523', 120.26825, 32.018168, 2079);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2081, '321283', '泰兴市', 3, '0523', 120.020228, 32.168784, 2079);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2082, '321281', '兴化市', 3, '0523', 119.840162, 32.938065, 2079);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2083, '321204', '姜堰区', 3, '0523', 120.148208, 32.508483, 2079);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2084, '321203', '高港区', 3, '0523', 119.88166, 32.315701, 2079);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2085, '321202', '海陵区', 3, '0523', 119.920187, 32.488406, 2079);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2086, '320900', '盐城市', 2, '0515', 120.139998, 33.377631, 2029);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2087, '320923', '阜宁县', 3, '0515', 119.805338, 33.78573, 2086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2088, '320922', '滨海县', 3, '0515', 119.828434, 33.989888, 2086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2089, '320924', '射阳县', 3, '0515', 120.257444, 33.773779, 2086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2090, '320925', '建湖县', 3, '0515', 119.793105, 33.472621, 2086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2091, '320902', '亭湖区', 3, '0515', 120.136078, 33.383912, 2086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2092, '320903', '盐都区', 3, '0515', 120.139753, 33.341288, 2086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2093, '320981', '东台市', 3, '0515', 120.314101, 32.853174, 2086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2094, '320904', '大丰区', 3, '0515', 120.470324, 33.199531, 2086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2095, '320921', '响水县', 3, '0515', 119.579573, 34.19996, 2086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2096, '321100', '镇江市', 2, '0511', 119.452753, 32.204402, 2029);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2097, '321182', '扬中市', 3, '0511', 119.828054, 32.237266, 2096);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2098, '321111', '润州区', 3, '0511', 119.414877, 32.213501, 2096);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2099, '321112', '丹徒区', 3, '0511', 119.433883, 32.128972, 2096);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2100, '321102', '京口区', 3, '0511', 119.454571, 32.206191, 2096);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2101, '321181', '丹阳市', 3, '0511', 119.581911, 31.991459, 2096);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2102, '321183', '句容市', 3, '0511', 119.167135, 31.947355, 2096);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2103, '320500', '苏州市', 2, '0512', 120.619585, 31.299379, 2029);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2104, '320582', '张家港市', 3, '0512', 120.543441, 31.865553, 2103);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2105, '320505', '虎丘区', 3, '0512', 120.566833, 31.294845, 2103);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2106, '320585', '太仓市', 3, '0512', 121.112275, 31.452568, 2103);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2107, '320508', '姑苏区', 3, '0512', 120.622249, 31.311414, 2103);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2108, '320509', '吴江区', 3, '0512', 120.641601, 31.160404, 2103);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2109, '320507', '相城区', 3, '0512', 120.618956, 31.396684, 2103);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2110, '320571', '苏州工业园区', 3, '0512', 120.723343, 31.324036, 2103);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2111, '320581', '常熟市', 3, '0512', 120.74852, 31.658156, 2103);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2112, '320506', '吴中区', 3, '0512', 120.624621, 31.270839, 2103);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2113, '320583', '昆山市', 3, '0512', 120.958137, 31.381925, 2103);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2114, '320200', '无锡市', 2, '0510', 120.301663, 31.574729, 2029);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2115, '320213', '梁溪区', 3, '0510', 120.296595, 31.575706, 2114);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2116, '320214', '新吴区', 3, '0510', 120.352782, 31.550966, 2114);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2117, '320281', '江阴市', 3, '0510', 120.275891, 31.910984, 2114);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2118, '320211', '滨湖区', 3, '0510', 120.266053, 31.550228, 2114);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2119, '320282', '宜兴市', 3, '0510', 119.820538, 31.364384, 2114);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2120, '320205', '锡山区', 3, '0510', 120.357298, 31.585559, 2114);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2121, '320206', '惠山区', 3, '0510', 120.303543, 31.681019, 2114);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2122, '320400', '常州市', 2, '0519', 119.946973, 31.772752, 2029);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2123, '320413', '金坛区', 3, '0519', 119.573395, 31.744399, 2122);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2124, '320481', '溧阳市', 3, '0519', 119.487816, 31.427081, 2122);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2125, '320411', '新北区', 3, '0519', 119.974654, 31.824664, 2122);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2126, '320412', '武进区', 3, '0519', 119.958773, 31.718566, 2122);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2127, '320404', '钟楼区', 3, '0519', 119.948388, 31.78096, 2122);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2128, '320402', '天宁区', 3, '0519', 119.963783, 31.779632, 2122);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2129, '320300', '徐州市', 2, '0516', 117.184811, 34.261792, 2029);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2130, '320381', '新沂市', 3, '0516', 118.345828, 34.368779, 2129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2131, '320311', '泉山区', 3, '0516', 117.182225, 34.262249, 2129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2132, '320312', '铜山区', 3, '0516', 117.183894, 34.19288, 2129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2133, '320303', '云龙区', 3, '0516', 117.194589, 34.254805, 2129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2134, '320302', '鼓楼区', 3, '0516', 117.192941, 34.269397, 2129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2135, '320324', '睢宁县', 3, '0516', 117.95066, 33.899222, 2129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2136, '320382', '邳州市', 3, '0516', 117.963923, 34.314708, 2129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2137, '320322', '沛县', 3, '0516', 116.937182, 34.729044, 2129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2138, '320321', '丰县', 3, '0516', 116.592888, 34.696946, 2129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2139, '320305', '贾汪区', 3, '0516', 117.450212, 34.441642, 2129);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2140, '330000', '浙江省', 1, '', 120.153576, 30.287459, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2141, '330900', '舟山市', 2, '0580', 122.106863, 30.016028, 2140);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2142, '330921', '岱山县', 3, '0580', 122.201132, 30.242865, 2141);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2143, '330922', '嵊泗县', 3, '0580', 122.457809, 30.727166, 2141);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2144, '330903', '普陀区', 3, '0580', 122.301953, 29.945614, 2141);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2145, '330902', '定海区', 3, '0580', 122.108496, 30.016423, 2141);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2146, '330400', '嘉兴市', 2, '0573', 120.750865, 30.762653, 2140);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2147, '330482', '平湖市', 3, '0573', 121.014666, 30.698921, 2146);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2148, '330402', '南湖区', 3, '0573', 120.749953, 30.764652, 2146);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2149, '330421', '嘉善县', 3, '0573', 120.921871, 30.841352, 2146);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2150, '330424', '海盐县', 3, '0573', 120.942017, 30.522223, 2146);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2151, '330483', '桐乡市', 3, '0573', 120.551085, 30.629065, 2146);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2152, '330411', '秀洲区', 3, '0573', 120.720431, 30.763323, 2146);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2153, '330481', '海宁市', 3, '0573', 120.688821, 30.525544, 2146);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2154, '330300', '温州市', 2, '0577', 120.672111, 28.000575, 2140);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2155, '330381', '瑞安市', 3, '0577', 120.646171, 27.779321, 2154);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2156, '330327', '苍南县', 3, '0577', 120.406256, 27.507743, 2154);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2157, '330305', '洞头区', 3, '0577', 121.156181, 27.836057, 2154);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2158, '330326', '平阳县', 3, '0577', 120.564387, 27.6693, 2154);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2159, '330383', '龙港市', 3, '0577', 120.553039, 27.578156, 2154);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2160, '330328', '文成县', 3, '0577', 120.09245, 27.789133, 2154);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2161, '330329', '泰顺县', 3, '0577', 119.71624, 27.557309, 2154);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2162, '330324', '永嘉县', 3, '0577', 120.690968, 28.153886, 2154);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2163, '330382', '乐清市', 3, '0577', 120.967147, 28.116083, 2154);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2164, '330302', '鹿城区', 3, '0577', 120.674231, 28.003352, 2154);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2165, '330304', '瓯海区', 3, '0577', 120.637145, 28.006444, 2154);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2166, '330303', '龙湾区', 3, '0577', 120.763469, 27.970254, 2154);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2167, '331000', '台州市', 2, '0576', 121.428599, 28.661378, 2140);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2168, '331004', '路桥区', 3, '0576', 121.37292, 28.581799, 2167);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2169, '331083', '玉环市', 3, '0576', 121.232337, 28.12842, 2167);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2170, '331002', '椒江区', 3, '0576', 121.431049, 28.67615, 2167);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2171, '331082', '临海市', 3, '0576', 121.131229, 28.845441, 2167);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2172, '331081', '温岭市', 3, '0576', 121.373611, 28.368781, 2167);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2173, '331022', '三门县', 3, '0576', 121.376429, 29.118955, 2167);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2174, '331023', '天台县', 3, '0576', 121.031227, 29.141126, 2167);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2175, '331003', '黄岩区', 3, '0576', 121.262138, 28.64488, 2167);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2176, '331024', '仙居县', 3, '0576', 120.735074, 28.849213, 2167);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2177, '330200', '宁波市', 2, '0574', 121.549792, 29.868388, 2140);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2178, '330206', '北仑区', 3, '0574', 121.831303, 29.90944, 2177);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2179, '330225', '象山县', 3, '0574', 121.877091, 29.470206, 2177);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2180, '330211', '镇海区', 3, '0574', 121.713162, 29.952107, 2177);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2181, '330205', '江北区', 3, '0574', 121.559282, 29.888361, 2177);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2182, '330213', '奉化区', 3, '0574', 121.41089, 29.662348, 2177);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2183, '330226', '宁海县', 3, '0574', 121.432606, 29.299836, 2177);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2184, '330203', '海曙区', 3, '0574', 121.539698, 29.874452, 2177);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2185, '330212', '鄞州区', 3, '0574', 121.558436, 29.831662, 2177);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2186, '330281', '余姚市', 3, '0574', 121.156294, 30.045404, 2177);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2187, '330282', '慈溪市', 3, '0574', 121.248052, 30.177142, 2177);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2188, '331100', '丽水市', 2, '0578', 119.921786, 28.451993, 2140);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2189, '331181', '龙泉市', 3, '0578', 119.132319, 28.069177, 2188);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2190, '331102', '莲都区', 3, '0578', 119.922293, 28.451103, 2188);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2191, '331124', '松阳县', 3, '0578', 119.485292, 28.449937, 2188);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2192, '331125', '云和县', 3, '0578', 119.569458, 28.111077, 2188);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2193, '331123', '遂昌县', 3, '0578', 119.27589, 28.5924, 2188);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2194, '331127', '景宁畲族自治县', 3, '0578', 119.634669, 27.977247, 2188);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2195, '331121', '青田县', 3, '0578', 120.291939, 28.135247, 2188);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2196, '331126', '庆元县', 3, '0578', 119.067233, 27.618231, 2188);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2197, '331122', '缙云县', 3, '0578', 120.078965, 28.654208, 2188);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2198, '330500', '湖州市', 2, '0572', 120.102398, 30.867198, 2140);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2199, '330521', '德清县', 3, '0572', 119.967662, 30.534927, 2198);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2200, '330503', '南浔区', 3, '0572', 120.417195, 30.872742, 2198);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2201, '330523', '安吉县', 3, '0572', 119.687891, 30.631974, 2198);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2202, '330522', '长兴县', 3, '0572', 119.910122, 31.00475, 2198);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2203, '330502', '吴兴区', 3, '0572', 120.101416, 30.867252, 2198);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2204, '330800', '衢州市', 2, '0570', 118.87263, 28.941708, 2140);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2205, '330822', '常山县', 3, '0570', 118.521654, 28.900039, 2204);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2206, '330824', '开化县', 3, '0570', 118.414435, 29.136503, 2204);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2207, '330802', '柯城区', 3, '0570', 118.873041, 28.944539, 2204);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2208, '330881', '江山市', 3, '0570', 118.627879, 28.734674, 2204);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2209, '330803', '衢江区', 3, '0570', 118.957683, 28.973195, 2204);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2210, '330825', '龙游县', 3, '0570', 119.172525, 29.031364, 2204);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2211, '330100', '杭州市', 2, '0571', 120.153576, 30.287459, 2140);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2212, '330102', '上城区', 3, '0571', 120.171465, 30.250236, 2211);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2213, '330111', '富阳区', 3, '0571', 119.949869, 30.049871, 2211);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2214, '330122', '桐庐县', 3, '0571', 119.685045, 29.797437, 2211);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2215, '330182', '建德市', 3, '0571', 119.279089, 29.472284, 2211);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2216, '330127', '淳安县', 3, '0571', 119.044276, 29.604177, 2211);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2217, '330108', '滨江区', 3, '0571', 120.21062, 30.206615, 2211);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2218, '330112', '临安区', 3, '0571', 119.715101, 30.231153, 2211);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2219, '330109', '萧山区', 3, '0571', 120.27069, 30.162932, 2211);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2220, '330104', '江干区', 3, '0571', 120.202633, 30.266603, 2211);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2221, '330110', '余杭区', 3, '0571', 120.301737, 30.421187, 2211);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2222, '330106', '西湖区', 3, '0571', 120.147376, 30.272934, 2211);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2223, '330105', '拱墅区', 3, '0571', 120.150053, 30.314697, 2211);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2224, '330103', '下城区', 3, '0571', 120.172763, 30.276271, 2211);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2225, '330700', '金华市', 2, '0579', 119.649506, 29.089524, 2140);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2226, '330726', '浦江县', 3, '0579', 119.893363, 29.451254, 2225);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2227, '330784', '永康市', 3, '0579', 120.036328, 28.895293, 2225);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2228, '330783', '东阳市', 3, '0579', 120.23334, 29.262546, 2225);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2229, '330723', '武义县', 3, '0579', 119.819159, 28.896563, 2225);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2230, '330781', '兰溪市', 3, '0579', 119.460521, 29.210065, 2225);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2231, '330727', '磐安县', 3, '0579', 120.44513, 29.052627, 2225);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2232, '330782', '义乌市', 3, '0579', 120.074911, 29.306863, 2225);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2233, '330703', '金东区', 3, '0579', 119.681264, 29.095835, 2225);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2234, '330702', '婺城区', 3, '0579', 119.652579, 29.082607, 2225);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2235, '330600', '绍兴市', 2, '0575', 120.582112, 29.997117, 2140);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2236, '330683', '嵊州市', 3, '0575', 120.82888, 29.586606, 2235);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2237, '330602', '越城区', 3, '0575', 120.585315, 29.996993, 2235);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2238, '330603', '柯桥区', 3, '0575', 120.476075, 30.078038, 2235);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2239, '330681', '诸暨市', 3, '0575', 120.244326, 29.713662, 2235);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2240, '330624', '新昌县', 3, '0575', 120.905665, 29.501205, 2235);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2241, '330604', '上虞区', 3, '0575', 120.874185, 30.016769, 2235);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2242, '360000', '江西省', 1, '', 115.892151, 28.676493, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2243, '361000', '抚州市', 2, '0794', 116.358351, 27.98385, 2242);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2244, '361003', '东乡区', 3, '0794', 116.605341, 28.2325, 2243);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2245, '361024', '崇仁县', 3, '0794', 116.059109, 27.760907, 2243);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2246, '361028', '资溪县', 3, '0794', 117.066095, 27.70653, 2243);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2247, '361002', '临川区', 3, '0794', 116.361404, 27.981919, 2243);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2248, '361026', '宜黄县', 3, '0794', 116.223023, 27.546512, 2243);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2249, '361022', '黎川县', 3, '0794', 116.91457, 27.292561, 2243);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2250, '361021', '南城县', 3, '0794', 116.63945, 27.55531, 2243);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2251, '361027', '金溪县', 3, '0794', 116.778751, 27.907387, 2243);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2252, '361025', '乐安县', 3, '0794', 115.838432, 27.420101, 2243);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2253, '361023', '南丰县', 3, '0794', 116.532994, 27.210132, 2243);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2254, '361030', '广昌县', 3, '0794', 116.327291, 26.838426, 2243);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2255, '360400', '九江市', 2, '0792', 115.992811, 29.712034, 2242);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2256, '360430', '彭泽县', 3, '0792', 116.55584, 29.898865, 2255);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2257, '360429', '湖口县', 3, '0792', 116.244313, 29.7263, 2255);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2258, '360428', '都昌县', 3, '0792', 116.205114, 29.275105, 2255);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2259, '360404', '柴桑区', 3, '0792', 115.892977, 29.610264, 2255);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2260, '360423', '武宁县', 3, '0792', 115.105646, 29.260182, 2255);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2261, '360424', '修水县', 3, '0792', 114.573428, 29.032729, 2255);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2262, '360483', '庐山市', 3, '0792', 116.043743, 29.456169, 2255);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2263, '360402', '濂溪区', 3, '0792', 115.99012, 29.676175, 2255);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2264, '360403', '浔阳区', 3, '0792', 115.995947, 29.72465, 2255);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2265, '360425', '永修县', 3, '0792', 115.809055, 29.018212, 2255);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2266, '360482', '共青城市', 3, '0792', 115.805712, 29.247884, 2255);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2267, '360426', '德安县', 3, '0792', 115.762611, 29.327474, 2255);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2268, '360481', '瑞昌市', 3, '0792', 115.669081, 29.676599, 2255);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2269, '360700', '赣州市', 2, '0797', 114.940278, 25.85097, 2242);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2270, '360732', '兴国县', 3, '0797', 115.351896, 26.330489, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2271, '360730', '宁都县', 3, '0797', 116.018782, 26.472054, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2272, '360735', '石城县', 3, '0797', 116.342249, 26.326582, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2273, '360725', '崇义县', 3, '0797', 114.307348, 25.687911, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2274, '360731', '于都县', 3, '0797', 115.411198, 25.955033, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2275, '360734', '寻乌县', 3, '0797', 115.651399, 24.954136, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2276, '360723', '大余县', 3, '0797', 114.362243, 25.395937, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2277, '360726', '安远县', 3, '0797', 115.392328, 25.134591, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2278, '360729', '全南县', 3, '0797', 114.531589, 24.742651, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2279, '360781', '瑞金市', 3, '0797', 116.034854, 25.875278, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2280, '360733', '会昌县', 3, '0797', 115.791158, 25.599125, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2281, '360722', '信丰县', 3, '0797', 114.930893, 25.38023, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2282, '360724', '上犹县', 3, '0797', 114.540537, 25.794284, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2283, '360728', '定南县', 3, '0797', 115.03267, 24.774277, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2284, '360703', '南康区', 3, '0797', 114.756933, 25.661721, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2285, '360727', '龙南县', 3, '0797', 114.792657, 24.90476, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2286, '360704', '赣县区', 3, '0797', 115.018461, 25.865432, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2287, '360702', '章贡区', 3, '0797', 114.93872, 25.851367, 2269);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2288, '360600', '鹰潭市', 2, '0701', 117.033838, 28.238638, 2242);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2289, '360681', '贵溪市', 3, '0701', 117.212103, 28.283693, 2288);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2290, '360603', '余江区', 3, '0701', 116.822763, 28.206177, 2288);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2291, '360602', '月湖区', 3, '0701', 117.034112, 28.239076, 2288);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2292, '360200', '景德镇市', 2, '0798', 117.214664, 29.29256, 2242);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2293, '360222', '浮梁县', 3, '0798', 117.217611, 29.352251, 2292);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2294, '360203', '珠山区', 3, '0798', 117.214814, 29.292812, 2292);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2295, '360202', '昌江区', 3, '0798', 117.195023, 29.288465, 2292);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2296, '360281', '乐平市', 3, '0798', 117.129376, 28.967361, 2292);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2297, '361100', '上饶市', 2, '0793', 117.971185, 28.44442, 2242);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2298, '361130', '婺源县', 3, '0793', 117.86219, 29.254015, 2297);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2299, '361181', '德兴市', 3, '0793', 117.578732, 28.945034, 2297);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2300, '361127', '余干县', 3, '0793', 116.691072, 28.69173, 2297);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2301, '361125', '横峰县', 3, '0793', 117.608247, 28.415103, 2297);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2302, '361126', '弋阳县', 3, '0793', 117.435002, 28.402391, 2297);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2303, '361103', '广丰区', 3, '0793', 118.189852, 28.440285, 2297);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2304, '361124', '铅山县', 3, '0793', 117.711906, 28.310892, 2297);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2305, '361123', '玉山县', 3, '0793', 118.244408, 28.673479, 2297);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2306, '361128', '鄱阳县', 3, '0793', 116.673748, 28.993374, 2297);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2307, '361129', '万年县', 3, '0793', 117.07015, 28.692589, 2297);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2308, '361104', '广信区', 3, '0793', 117.90612, 28.453897, 2297);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2309, '361102', '信州区', 3, '0793', 117.970522, 28.445378, 2297);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2310, '360300', '萍乡市', 2, '0799', 113.852186, 27.622946, 2242);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2311, '360302', '安源区', 3, '0799', 113.855044, 27.625826, 2310);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2312, '360323', '芦溪县', 3, '0799', 114.041206, 27.633633, 2310);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2313, '360322', '上栗县', 3, '0799', 113.800525, 27.877041, 2310);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2314, '360321', '莲花县', 3, '0799', 113.955582, 27.127807, 2310);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2315, '360313', '湘东区', 3, '0799', 113.7456, 27.639319, 2310);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2316, '360900', '宜春市', 2, '0795', 114.391136, 27.8043, 2242);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2317, '360921', '奉新县', 3, '0795', 115.389899, 28.700672, 2316);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2318, '360924', '宜丰县', 3, '0795', 114.787381, 28.388289, 2316);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2319, '360983', '高安市', 3, '0795', 115.381527, 28.420951, 2316);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2320, '360981', '丰城市', 3, '0795', 115.786005, 28.191584, 2316);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2321, '360923', '上高县', 3, '0795', 114.932653, 28.234789, 2316);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2322, '360926', '铜鼓县', 3, '0795', 114.37014, 28.520956, 2316);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2323, '360902', '袁州区', 3, '0795', 114.387379, 27.800117, 2316);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2324, '360922', '万载县', 3, '0795', 114.449012, 28.104528, 2316);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2325, '360982', '樟树市', 3, '0795', 115.543388, 28.055898, 2316);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2326, '360925', '靖安县', 3, '0795', 115.361744, 28.86054, 2316);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2327, '360100', '南昌市', 2, '0791', 115.892151, 28.676493, 2242);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2328, '360124', '进贤县', 3, '0791', 116.267671, 28.365681, 2327);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2329, '360104', '青云谱区', 3, '0791', 115.907292, 28.635724, 2327);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2330, '360121', '南昌县', 3, '0791', 115.942465, 28.543781, 2327);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2331, '360103', '西湖区', 3, '0791', 115.91065, 28.662901, 2327);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2332, '360111', '青山湖区', 3, '0791', 115.949044, 28.689292, 2327);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2333, '360102', '东湖区', 3, '0791', 115.889675, 28.682988, 2327);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2334, '360112', '新建区', 3, '0791', 115.820806, 28.690788, 2327);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2335, '360113', '红谷滩区', 3, '0791', 115.8580521, 28.69819928, 2327);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2336, '360123', '安义县', 3, '0791', 115.553109, 28.841334, 2327);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2337, '360800', '吉安市', 2, '0796', 114.986373, 27.111699, 2242);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2338, '360821', '吉安县', 3, '0796', 114.905117, 27.040042, 2337);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2339, '360825', '永丰县', 3, '0796', 115.435559, 27.321087, 2337);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2340, '360822', '吉水县', 3, '0796', 115.134569, 27.213445, 2337);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2341, '360830', '永新县', 3, '0796', 114.242534, 26.944721, 2337);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2342, '360827', '遂川县', 3, '0796', 114.51689, 26.323705, 2337);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2343, '360802', '吉州区', 3, '0796', 114.987331, 27.112367, 2337);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2344, '360828', '万安县', 3, '0796', 114.784694, 26.462085, 2337);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2345, '360803', '青原区', 3, '0796', 115.016306, 27.105879, 2337);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2346, '360829', '安福县', 3, '0796', 114.61384, 27.382746, 2337);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2347, '360824', '新干县', 3, '0796', 115.399294, 27.755758, 2337);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2348, '360823', '峡江县', 3, '0796', 115.319331, 27.580862, 2337);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2349, '360881', '井冈山市', 3, '0796', 114.284421, 26.745919, 2337);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2350, '360826', '泰和县', 3, '0796', 114.901393, 26.790164, 2337);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2351, '360500', '新余市', 2, '0790', 114.930835, 27.810834, 2242);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2352, '360521', '分宜县', 3, '0790', 114.675262, 27.811301, 2351);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2353, '360502', '渝水区', 3, '0790', 114.923923, 27.819171, 2351);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2354, '710000', '台湾省', 1, '1886', 121.509062, 25.044332, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2355, '130000', '河北省', 1, '', 114.502461, 38.045474, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2356, '130200', '唐山市', 2, '0315', 118.175393, 39.635113, 2355);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2357, '130281', '遵化市', 3, '0315', 117.965875, 40.188616, 2356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2358, '130227', '迁西县', 3, '0315', 118.305139, 40.146238, 2356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2359, '130283', '迁安市', 3, '0315', 118.701933, 40.012108, 2356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2360, '130224', '滦南县', 3, '0315', 118.681552, 39.506201, 2356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2361, '130209', '曹妃甸区', 3, '0315', 118.446585, 39.278277, 2356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2362, '130284', '滦州市', 3, '0315', 118.699546, 39.74485, 2356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2363, '130225', '乐亭县', 3, '0315', 118.905341, 39.42813, 2356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2364, '130229', '玉田县', 3, '0315', 117.753665, 39.887323, 2356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2365, '130202', '路南区', 3, '0315', 118.210821, 39.615162, 2356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2366, '130207', '丰南区', 3, '0315', 118.110793, 39.56303, 2356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2367, '130203', '路北区', 3, '0315', 118.174736, 39.628538, 2356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2368, '130208', '丰润区', 3, '0315', 118.155779, 39.831363, 2356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2369, '130204', '古冶区', 3, '0315', 118.45429, 39.715736, 2356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2370, '130205', '开平区', 3, '0315', 118.264425, 39.676171, 2356);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2371, '131000', '廊坊市', 2, '0316', 116.704441, 39.523927, 2355);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2372, '131028', '大厂回族自治县', 3, '0316', 116.986501, 39.889266, 2371);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2373, '131025', '大城县', 3, '0316', 116.640735, 38.699215, 2371);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2374, '131024', '香河县', 3, '0316', 117.007161, 39.757212, 2371);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2375, '131026', '文安县', 3, '0316', 116.460107, 38.866801, 2371);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2376, '131023', '永清县', 3, '0316', 116.498089, 39.319717, 2371);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2377, '131022', '固安县', 3, '0316', 116.299894, 39.436468, 2371);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2378, '131003', '广阳区', 3, '0316', 116.713708, 39.521931, 2371);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2379, '131082', '三河市', 3, '0316', 117.077018, 39.982778, 2371);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2380, '131002', '安次区', 3, '0316', 116.694544, 39.502569, 2371);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2381, '131081', '霸州市', 3, '0316', 116.392021, 39.117331, 2371);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2382, '130300', '秦皇岛市', 2, '0335', 119.586579, 39.942531, 2355);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2383, '130321', '青龙满族自治县', 3, '0335', 118.954555, 40.406023, 2382);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2384, '130324', '卢龙县', 3, '0335', 118.881809, 39.891639, 2382);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2385, '130303', '山海关区', 3, '0335', 119.753591, 39.998023, 2382);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2386, '130306', '抚宁区', 3, '0335', 119.240651, 39.887053, 2382);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2387, '130302', '海港区', 3, '0335', 119.596224, 39.943458, 2382);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2388, '130304', '北戴河区', 3, '0335', 119.486286, 39.825121, 2382);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2389, '130322', '昌黎县', 3, '0335', 119.164541, 39.709729, 2382);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2390, '130700', '张家口市', 2, '0313', 114.884091, 40.811901, 2355);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2391, '130722', '张北县', 3, '0313', 114.715951, 41.151713, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2392, '130723', '康保县', 3, '0313', 114.615809, 41.850046, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2393, '130724', '沽源县', 3, '0313', 115.684836, 41.667419, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2394, '130725', '尚义县', 3, '0313', 113.977713, 41.080091, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2395, '130709', '崇礼区', 3, '0313', 115.281652, 40.971302, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2396, '130708', '万全区', 3, '0313', 114.736131, 40.765136, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2397, '130732', '赤城县', 3, '0313', 115.832708, 40.912081, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2398, '130730', '怀来县', 3, '0313', 115.520846, 40.405405, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2399, '130706', '下花园区', 3, '0313', 115.281002, 40.488645, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2400, '130731', '涿鹿县', 3, '0313', 115.219246, 40.378701, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2401, '130705', '宣化区', 3, '0313', 115.0632, 40.609368, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2402, '130703', '桥西区', 3, '0313', 114.882127, 40.824385, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2403, '130702', '桥东区', 3, '0313', 114.885658, 40.813875, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2404, '130728', '怀安县', 3, '0313', 114.422364, 40.671274, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2405, '130726', '蔚县', 3, '0313', 114.582695, 39.837181, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2406, '130727', '阳原县', 3, '0313', 114.167343, 40.113419, 2390);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2407, '130900', '沧州市', 2, '0317', 116.857461, 38.310582, 2355);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2408, '130982', '任丘市', 3, '0317', 116.106764, 38.706513, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2409, '130929', '献县', 3, '0317', 116.123844, 38.189661, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2410, '130902', '新华区', 3, '0317', 116.873049, 38.308273, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2411, '130903', '运河区', 3, '0317', 116.840063, 38.307405, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2412, '130924', '海兴县', 3, '0317', 117.496606, 38.141582, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2413, '130927', '南皮县', 3, '0317', 116.709171, 38.042439, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2414, '130923', '东光县', 3, '0317', 116.542062, 37.88655, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2415, '130928', '吴桥县', 3, '0317', 116.391512, 37.628182, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2416, '130983', '黄骅市', 3, '0317', 117.343803, 38.369238, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2417, '130922', '青县', 3, '0317', 116.838384, 38.569646, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2418, '130921', '沧县', 3, '0317', 117.007478, 38.219856, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2419, '130981', '泊头市', 3, '0317', 116.570163, 38.073479, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2420, '130984', '河间市', 3, '0317', 116.089452, 38.44149, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2421, '130926', '肃宁县', 3, '0317', 115.835856, 38.427102, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2422, '130930', '孟村回族自治县', 3, '0317', 117.105104, 38.057953, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2423, '130925', '盐山县', 3, '0317', 117.229814, 38.056141, 2407);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2424, '130400', '邯郸市', 2, '0310', 114.490686, 36.612273, 2355);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2425, '130481', '武安市', 3, '0310', 114.194581, 36.696115, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2426, '130423', '临漳县', 3, '0310', 114.610703, 36.337604, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2427, '130434', '魏县', 3, '0310', 114.93411, 36.354248, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2428, '130406', '峰峰矿区', 3, '0310', 114.209936, 36.420487, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2429, '130407', '肥乡区', 3, '0310', 114.805154, 36.555778, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2430, '130426', '涉县', 3, '0310', 113.673297, 36.563143, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2431, '130431', '鸡泽县', 3, '0310', 114.878517, 36.914908, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2432, '130435', '曲周县', 3, '0310', 114.957588, 36.773398, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2433, '130427', '磁县', 3, '0310', 114.38208, 36.367673, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2434, '130404', '复兴区', 3, '0310', 114.458242, 36.615484, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2435, '130403', '丛台区', 3, '0310', 114.494703, 36.611082, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2436, '130408', '永年区', 3, '0310', 114.496162, 36.776413, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2437, '130402', '邯山区', 3, '0310', 114.484989, 36.603196, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2438, '130424', '成安县', 3, '0310', 114.680356, 36.443832, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2439, '130433', '馆陶县', 3, '0310', 115.289057, 36.539461, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2440, '130430', '邱县', 3, '0310', 115.168584, 36.81325, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2441, '130425', '大名县', 3, '0310', 115.152586, 36.283316, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2442, '130432', '广平县', 3, '0310', 114.950859, 36.483603, 2424);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2443, '130500', '邢台市', 2, '0319', 114.508851, 37.0682, 2355);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2444, '130523', '内丘县', 3, '0319', 114.511523, 37.287663, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2445, '130524', '柏乡县', 3, '0319', 114.693382, 37.483596, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2446, '130530', '新河县', 3, '0319', 115.247537, 37.526216, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2447, '130527', '南和区', 3, '0319', 114.691377, 37.003812, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2448, '130581', '南宫市', 3, '0319', 115.398102, 37.359668, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2449, '130532', '平乡县', 3, '0319', 115.029218, 37.069404, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2450, '130582', '沙河市', 3, '0319', 114.504902, 36.861903, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2451, '130531', '广宗县', 3, '0319', 115.142797, 37.075548, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2452, '130534', '清河县', 3, '0319', 115.668999, 37.059991, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2453, '130522', '临城县', 3, '0319', 114.506873, 37.444009, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2454, '130533', '威县', 3, '0319', 115.272749, 36.983272, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2455, '130535', '临西县', 3, '0319', 115.498684, 36.8642, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2456, '130528', '宁晋县', 3, '0319', 114.921027, 37.618956, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2457, '130525', '隆尧县', 3, '0319', 114.776348, 37.350925, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2458, '130526', '任泽区', 3, '0319', 114.684469, 37.129952, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2459, '130529', '巨鹿县', 3, '0319', 115.038782, 37.21768, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2460, '130503', '信都区', 3, '0319', 114.473687, 37.068009, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2461, '130502', '襄都区', 3, '0319', 114.507131, 37.064125, 2443);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2462, '130100', '石家庄市', 2, '0311', 114.502461, 38.045474, 2355);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2463, '130123', '正定县', 3, '0311', 114.569887, 38.147835, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2464, '130109', '藁城区', 3, '0311', 114.849647, 38.033767, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2465, '130131', '平山县', 3, '0311', 114.184144, 38.259311, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2466, '130126', '灵寿县', 3, '0311', 114.37946, 38.306546, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2467, '130130', '无极县', 3, '0311', 114.977845, 38.176376, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2468, '130127', '高邑县', 3, '0311', 114.610699, 37.605714, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2469, '130132', '元氏县', 3, '0311', 114.52618, 37.762514, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2470, '130129', '赞皇县', 3, '0311', 114.387756, 37.660199, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2471, '130104', '桥西区', 3, '0311', 114.462931, 38.028383, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2472, '130110', '鹿泉区', 3, '0311', 114.321023, 38.093994, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2473, '130108', '裕华区', 3, '0311', 114.533257, 38.027696, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2474, '130102', '长安区', 3, '0311', 114.548151, 38.047501, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2475, '130183', '晋州市', 3, '0311', 115.044886, 38.027478, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2476, '130121', '井陉县', 3, '0311', 114.144488, 38.033614, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2477, '130107', '井陉矿区', 3, '0311', 114.058178, 38.069748, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2478, '130181', '辛集市', 3, '0311', 115.217451, 37.92904, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2479, '130184', '新乐市', 3, '0311', 114.68578, 38.344768, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2480, '130105', '新华区', 3, '0311', 114.465974, 38.067142, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2481, '130128', '深泽县', 3, '0311', 115.200207, 38.18454, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2482, '130133', '赵县', 3, '0311', 114.775362, 37.754341, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2483, '130111', '栾城区', 3, '0311', 114.654281, 37.886911, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2484, '130125', '行唐县', 3, '0311', 114.552734, 38.437422, 2462);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2485, '131100', '衡水市', 2, '0318', 115.665993, 37.735097, 2355);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2486, '131122', '武邑县', 3, '0318', 115.892415, 37.803774, 2485);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2487, '131123', '武强县', 3, '0318', 115.970236, 38.03698, 2485);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2488, '131121', '枣强县', 3, '0318', 115.726499, 37.511512, 2485);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2489, '131102', '桃城区', 3, '0318', 115.694945, 37.732237, 2485);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2490, '131182', '深州市', 3, '0318', 115.554596, 38.00347, 2485);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2491, '131128', '阜城县', 3, '0318', 116.164727, 37.869945, 2485);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2492, '131125', '安平县', 3, '0318', 115.519627, 38.233511, 2485);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2493, '131124', '饶阳县', 3, '0318', 115.726577, 38.232671, 2485);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2494, '131103', '冀州区', 3, '0318', 115.579173, 37.542788, 2485);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2495, '131127', '景县', 3, '0318', 116.258446, 37.686622, 2485);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2496, '131126', '故城县', 3, '0318', 115.966747, 37.350981, 2485);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2497, '130600', '保定市', 2, '0312', 115.482331, 38.867657, 2355);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2498, '130630', '涞源县', 3, '0312', 114.692567, 39.35755, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2499, '130632', '安新县', 3, '0312', 115.931979, 38.929912, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2500, '130633', '易县', 3, '0312', 115.501146, 39.35297, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2501, '130609', '徐水区', 3, '0312', 115.64941, 39.020395, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2502, '130636', '顺平县', 3, '0312', 115.132749, 38.845127, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2503, '130628', '高阳县', 3, '0312', 115.778878, 38.690092, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2504, '130602', '竞秀区', 3, '0312', 115.470659, 38.88662, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2505, '130607', '满城区', 3, '0312', 115.32442, 38.95138, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2506, '130682', '定州市', 3, '0312', 114.991389, 38.517602, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2507, '130683', '安国市', 3, '0312', 115.33141, 38.421367, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2508, '130606', '莲池区', 3, '0312', 115.500934, 38.865005, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2509, '130623', '涞水县', 3, '0312', 115.711985, 39.393148, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2510, '130681', '涿州市', 3, '0312', 115.973409, 39.485765, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2511, '130637', '博野县', 3, '0312', 115.461798, 38.458271, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2512, '130635', '蠡县', 3, '0312', 115.583631, 38.496429, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2513, '130624', '阜平县', 3, '0312', 114.198801, 38.847276, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2514, '130608', '清苑区', 3, '0312', 115.492221, 38.771012, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2515, '130631', '望都县', 3, '0312', 115.154009, 38.707448, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2516, '130634', '曲阳县', 3, '0312', 114.704055, 38.619992, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2517, '130627', '唐县', 3, '0312', 114.981241, 38.748542, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2518, '130684', '高碑店市', 3, '0312', 115.882704, 39.327689, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2519, '130629', '容城县', 3, '0312', 115.866247, 39.05282, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2520, '130626', '定兴县', 3, '0312', 115.796895, 39.266195, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2521, '130638', '雄县', 3, '0312', 116.107474, 38.990819, 2497);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2522, '130800', '承德市', 2, '0314', 117.939152, 40.976204, 2355);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2523, '130828', '围场满族蒙古族自治县', 3, '0314', 117.764086, 41.949404, 2522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2524, '130826', '丰宁满族自治县', 3, '0314', 116.65121, 41.209903, 2522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2525, '130825', '隆化县', 3, '0314', 117.736343, 41.316667, 2522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2526, '130827', '宽城满族自治县', 3, '0314', 118.488642, 40.607981, 2522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2527, '130804', '鹰手营子矿区', 3, '0314', 117.661154, 40.546956, 2522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2528, '130881', '平泉市', 3, '0314', 118.690238, 41.00561, 2522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2529, '130822', '兴隆县', 3, '0314', 117.507098, 40.418525, 2522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2530, '130824', '滦平县', 3, '0314', 117.337124, 40.936644, 2522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2531, '130803', '双滦区', 3, '0314', 117.797485, 40.959756, 2522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2532, '130802', '双桥区', 3, '0314', 117.939152, 40.976204, 2522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2533, '130821', '承德县', 3, '0314', 118.172496, 40.768637, 2522);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2534, '810000', '香港特别行政区', 1, '1852', 114.173355, 22.320048, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2535, '810016', '沙田区', 3, '1852', 114.1953653, 22.37953167, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2536, '810005', '油尖旺区', 3, '1852', 114.1733317, 22.31170389, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2537, '810009', '观塘区', 3, '1852', 114.2140542, 22.32083778, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2538, '810011', '屯门区', 3, '1852', 113.9765742, 22.39384417, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2539, '810014', '大埔区', 3, '1852', 114.1717431, 22.44565306, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2540, '810004', '南区', 3, '1852', 114.1600117, 22.24589667, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2541, '810007', '九龙城区', 3, '1852', 114.1928467, 22.31251, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2542, '810015', '西贡区', 3, '1852', 114.264645, 22.31421306, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2543, '810006', '深水埗区', 3, '1852', 114.1632417, 22.33385417, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2544, '810008', '黄大仙区', 3, '1852', 114.2038856, 22.33632056, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2545, '810001', '中西区', 3, '1852', 114.1543731, 22.28198083, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2546, '810013', '北区', 3, '1852', 114.1473639, 22.49610389, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2547, '810012', '元朗区', 3, '1852', 114.0324381, 22.44142833, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2548, '810002', '湾仔区', 3, '1852', 114.1829153, 22.27638889, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2549, '810018', '离岛区', 3, '1852', 113.94612, 22.28640778, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2550, '810003', '东区', 3, '1852', 114.2260031, 22.27969306, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2551, '810010', '荃湾区', 3, '1852', 114.1210792, 22.36830667, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2552, '810017', '葵青区', 3, '1852', 114.1393194, 22.36387667, 2534);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2553, '820000', '澳门特别行政区', 1, '1853', 113.54909, 22.198951, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2554, '820001', '花地玛堂区', 3, '1853', 113.5528956, 22.20787, 2553);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2555, '820007', '路凼填海区', 3, '1853', 113.5695992, 22.13663, 2553);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2556, '820003', '望德堂区', 3, '1853', 113.5501828, 22.19372083, 2553);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2557, '820002', '花王堂区', 3, '1853', 113.5489608, 22.1992075, 2553);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2558, '820004', '大堂区', 3, '1853', 113.5536475, 22.18853944, 2553);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2559, '820008', '圣方济各堂区', 3, '1853', 113.5599542, 22.12348639, 2553);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2560, '820006', '嘉模堂区', 3, '1853', 113.5587044, 22.15375944, 2553);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2561, '820005', '风顺堂区', 3, '1853', 113.5419278, 22.18736806, 2553);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2562, '620000', '甘肃省', 1, '', 103.823557, 36.058039, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2563, '620300', '金昌市', 2, '0935', 102.187888, 38.514238, 2562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2564, '620321', '永昌县', 3, '0935', 101.971957, 38.247354, 2563);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2565, '620302', '金川区', 3, '0935', 102.187683, 38.513793, 2563);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2566, '620400', '白银市', 2, '0943', 104.173606, 36.54568, 2562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2567, '620421', '靖远县', 3, '0943', 104.686972, 36.561424, 2566);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2568, '620403', '平川区', 3, '0943', 104.819207, 36.72921, 2566);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2569, '620402', '白银区', 3, '0943', 104.17425, 36.545649, 2566);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2570, '620423', '景泰县', 3, '0943', 104.066394, 37.193519, 2566);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2571, '620422', '会宁县', 3, '0943', 105.054337, 35.692486, 2566);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2572, '620900', '酒泉市', 2, '0937', 98.510795, 39.744023, 2562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2573, '620921', '金塔县', 3, '0937', 98.902959, 39.983036, 2572);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2574, '620923', '肃北蒙古族自治县', 3, '0937', 94.87728, 39.51224, 2572);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2575, '620981', '玉门市', 3, '0937', 97.037206, 40.28682, 2572);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2576, '620982', '敦煌市', 3, '0937', 94.664279, 40.141119, 2572);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2577, '620902', '肃州区', 3, '0937', 98.511155, 39.743858, 2572);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2578, '620922', '瓜州县', 3, '0937', 95.780591, 40.516525, 2572);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2579, '620924', '阿克塞哈萨克族自治县', 3, '0937', 94.337642, 39.631642, 2572);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2580, '621200', '陇南市', 2, '2935', 104.929379, 33.388598, 2562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2581, '621226', '礼县', 3, '2935', 105.181616, 34.189387, 2580);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2582, '621223', '宕昌县', 3, '2935', 104.394475, 34.042655, 2580);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2583, '621202', '武都区', 3, '2935', 104.929866, 33.388155, 2580);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2584, '621224', '康县', 3, '2935', 105.609534, 33.328266, 2580);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2585, '621228', '两当县', 3, '2935', 106.306959, 33.910729, 2580);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2586, '621227', '徽县', 3, '2935', 106.085632, 33.767785, 2580);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2587, '621221', '成县', 3, '2935', 105.734434, 33.739863, 2580);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2588, '621222', '文县', 3, '2935', 104.682448, 32.942171, 2580);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2589, '621225', '西和县', 3, '2935', 105.299737, 34.013718, 2580);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2590, '620800', '平凉市', 2, '0933', 106.684691, 35.54279, 2562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2591, '620822', '灵台县', 3, '0933', 107.620587, 35.064009, 2590);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2592, '620802', '崆峒区', 3, '0933', 106.684223, 35.54173, 2590);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2593, '620825', '庄浪县', 3, '0933', 106.041979, 35.203428, 2590);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2594, '620823', '崇信县', 3, '0933', 107.031253, 35.304533, 2590);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2595, '620826', '静宁县', 3, '0933', 105.733489, 35.525243, 2590);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2596, '620881', '华亭市', 3, '0933', 106.649308, 35.215341, 2590);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2597, '620821', '泾川县', 3, '0933', 107.365218, 35.335283, 2590);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2598, '620600', '武威市', 2, '1935', 102.634697, 37.929996, 2562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2599, '620622', '古浪县', 3, '1935', 102.898047, 37.470571, 2598);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2600, '620602', '凉州区', 3, '1935', 102.634492, 37.93025, 2598);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2601, '620623', '天祝藏族自治县', 3, '1935', 103.142034, 36.971678, 2598);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2602, '620621', '民勤县', 3, '1935', 103.090654, 38.624621, 2598);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2603, '620100', '兰州市', 2, '0931', 103.823557, 36.058039, 2562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2604, '620121', '永登县', 3, '0931', 103.262203, 36.734428, 2603);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2605, '620122', '皋兰县', 3, '0931', 103.94933, 36.331254, 2603);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2606, '620104', '西固区', 3, '0931', 103.622331, 36.100369, 2603);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2607, '620111', '红古区', 3, '0931', 102.861814, 36.344177, 2603);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2608, '620105', '安宁区', 3, '0931', 103.724038, 36.10329, 2603);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2609, '620102', '城关区', 3, '0931', 103.841032, 36.049115, 2603);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2610, '620103', '七里河区', 3, '0931', 103.784326, 36.06673, 2603);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2611, '620123', '榆中县', 3, '0931', 104.114975, 35.84443, 2603);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2612, '620200', '嘉峪关市', 2, '1937', 98.277304, 39.786529, 2562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2613, '623000', '甘南藏族自治州', 2, '0941', 102.911008, 34.986354, 2562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2614, '623022', '卓尼县', 3, '0941', 103.508508, 34.588165, 2613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2615, '623027', '夏河县', 3, '0941', 102.520743, 35.200853, 2613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2616, '623001', '合作市', 3, '0941', 102.91149, 34.985973, 2613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2617, '623021', '临潭县', 3, '0941', 103.353054, 34.69164, 2613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2618, '623025', '玛曲县', 3, '0941', 102.075767, 33.998068, 2613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2619, '623024', '迭部县', 3, '0941', 103.221009, 34.055348, 2613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2620, '623023', '舟曲县', 3, '0941', 104.370271, 33.782964, 2613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2621, '623026', '碌曲县', 3, '0941', 102.488495, 34.589591, 2613);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2622, '622900', '临夏回族自治州', 2, '0930', 103.212006, 35.599446, 2562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2623, '622927', '积石山保安族东乡族撒拉族自治县', 3, '0930', 102.877473, 35.712906, 2622);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2624, '622926', '东乡族自治县', 3, '0930', 103.389568, 35.66383, 2622);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2625, '622923', '永靖县', 3, '0930', 103.319871, 35.938933, 2622);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2626, '622901', '临夏市', 3, '0930', 103.211634, 35.59941, 2622);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2627, '622921', '临夏县', 3, '0930', 102.993873, 35.49236, 2622);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2628, '622924', '广河县', 3, '0930', 103.576188, 35.481688, 2622);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2629, '622925', '和政县', 3, '0930', 103.350357, 35.425971, 2622);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2630, '622922', '康乐县', 3, '0930', 103.709852, 35.371906, 2622);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2631, '620700', '张掖市', 2, '0936', 100.455472, 38.932897, 2562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2632, '620724', '高台县', 3, '0936', 99.81665, 39.376308, 2631);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2633, '620721', '肃南裕固族自治县', 3, '0936', 99.617086, 38.837269, 2631);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2634, '620722', '民乐县', 3, '0936', 100.816623, 38.434454, 2631);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2635, '620725', '山丹县', 3, '0936', 101.088442, 38.784839, 2631);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2636, '620723', '临泽县', 3, '0936', 100.166333, 39.152151, 2631);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2637, '620702', '甘州区', 3, '0936', 100.454862, 38.931774, 2631);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2638, '621000', '庆阳市', 2, '0934', 107.638372, 35.734218, 2562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2639, '621025', '正宁县', 3, '0934', 108.361068, 35.490642, 2638);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2640, '621027', '镇原县', 3, '0934', 107.195706, 35.677806, 2638);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2641, '621023', '华池县', 3, '0934', 107.986288, 36.457304, 2638);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2642, '621022', '环县', 3, '0934', 107.308754, 36.569322, 2638);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2643, '621026', '宁县', 3, '0934', 107.921182, 35.50201, 2638);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2644, '621002', '西峰区', 3, '0934', 107.638824, 35.733713, 2638);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2645, '621021', '庆城县', 3, '0934', 107.885664, 36.013504, 2638);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2646, '621024', '合水县', 3, '0934', 108.019865, 35.819005, 2638);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2647, '621100', '定西市', 2, '0932', 104.626294, 35.579578, 2562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2648, '621123', '渭源县', 3, '0932', 104.211742, 35.133023, 2647);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2649, '621124', '临洮县', 3, '0932', 103.862186, 35.376233, 2647);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2650, '621126', '岷县', 3, '0932', 104.039882, 34.439105, 2647);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2651, '621125', '漳县', 3, '0932', 104.466756, 34.848642, 2647);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2652, '621102', '安定区', 3, '0932', 104.62577, 35.579764, 2647);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2653, '621122', '陇西县', 3, '0932', 104.637554, 35.003409, 2647);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2654, '621121', '通渭县', 3, '0932', 105.250102, 35.208922, 2647);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2655, '620500', '天水市', 2, '0938', 105.724998, 34.578529, 2562);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2656, '620523', '甘谷县', 3, '0938', 105.332347, 34.747327, 2655);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2657, '620522', '秦安县', 3, '0938', 105.6733, 34.862354, 2655);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2658, '620502', '秦州区', 3, '0938', 105.724477, 34.578645, 2655);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2659, '620503', '麦积区', 3, '0938', 105.897631, 34.563504, 2655);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2660, '620525', '张家川回族自治县', 3, '0938', 106.212416, 34.993237, 2655);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2661, '620521', '清水县', 3, '0938', 106.139878, 34.75287, 2655);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2662, '620524', '武山县', 3, '0938', 104.891696, 34.721955, 2655);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2663, '510000', '四川省', 1, '', 104.065735, 30.659462, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2664, '511900', '巴中市', 2, '0827', 106.753669, 31.858809, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2665, '511902', '巴州区', 3, '0827', 106.753671, 31.858366, 2664);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2666, '511923', '平昌县', 3, '0827', 107.101937, 31.562814, 2664);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2667, '511921', '通江县', 3, '0827', 107.247621, 31.91212, 2664);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2668, '511922', '南江县', 3, '0827', 106.843418, 32.353164, 2664);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2669, '511903', '恩阳区', 3, '0827', 106.486515, 31.816336, 2664);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2670, '510100', '成都市', 2, '028', 104.065735, 30.659462, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2671, '510182', '彭州市', 3, '028', 103.941173, 30.985161, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2672, '510181', '都江堰市', 3, '028', 103.627898, 30.99114, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2673, '510129', '大邑县', 3, '028', 103.522397, 30.586602, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2674, '510131', '蒲江县', 3, '028', 103.511541, 30.194359, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2675, '510113', '青白江区', 3, '028', 104.25494, 30.883438, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2676, '510185', '简阳市', 3, '028', 104.550339, 30.390666, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2677, '510184', '崇州市', 3, '028', 103.671049, 30.631478, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2678, '510121', '金堂县', 3, '028', 104.415604, 30.858417, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2679, '510183', '邛崃市', 3, '028', 103.46143, 30.413271, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2680, '510132', '新津区', 3, '028', 103.812449, 30.414284, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2681, '510115', '温江区', 3, '028', 103.836776, 30.697996, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2682, '510117', '郫都区', 3, '028', 103.887842, 30.808752, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2683, '510116', '双流区', 3, '028', 103.922706, 30.573243, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2684, '510107', '武侯区', 3, '028', 104.05167, 30.630862, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2685, '510106', '金牛区', 3, '028', 104.043487, 30.692058, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2686, '510114', '新都区', 3, '028', 104.16022, 30.824223, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2687, '510112', '龙泉驿区', 3, '028', 104.269181, 30.56065, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2688, '510108', '成华区', 3, '028', 104.103077, 30.660275, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2689, '510105', '青羊区', 3, '028', 104.055731, 30.667648, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2690, '510104', '锦江区', 3, '028', 104.080989, 30.657689, 2670);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2691, '510600', '德阳市', 2, '0838', 104.398651, 31.127991, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2692, '510682', '什邡市', 3, '0838', 104.173653, 31.126881, 2691);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2693, '510623', '中江县', 3, '0838', 104.677831, 31.03681, 2691);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2694, '510681', '广汉市', 3, '0838', 104.281903, 30.97715, 2691);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2695, '510603', '旌阳区', 3, '0838', 104.389648, 31.130428, 2691);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2696, '510604', '罗江区', 3, '0838', 104.507126, 31.303281, 2691);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2697, '510683', '绵竹市', 3, '0838', 104.200162, 31.343084, 2691);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2698, '510800', '广元市', 2, '0839', 105.829757, 32.433668, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2699, '510811', '昭化区', 3, '0839', 105.964121, 32.322788, 2698);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2700, '510823', '剑阁县', 3, '0839', 105.527035, 32.286517, 2698);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2701, '510812', '朝天区', 3, '0839', 105.88917, 32.642632, 2698);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2702, '510824', '苍溪县', 3, '0839', 105.939706, 31.732251, 2698);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2703, '510821', '旺苍县', 3, '0839', 106.290426, 32.22833, 2698);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2704, '510822', '青川县', 3, '0839', 105.238847, 32.585655, 2698);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2705, '510802', '利州区', 3, '0839', 105.826194, 32.432276, 2698);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2706, '510900', '遂宁市', 2, '0825', 105.571331, 30.513311, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2707, '510923', '大英县', 3, '0825', 105.252187, 30.581571, 2706);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2708, '510981', '射洪市', 3, '0825', 105.381849, 30.868752, 2706);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2709, '510921', '蓬溪县', 3, '0825', 105.713699, 30.774883, 2706);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2710, '510904', '安居区', 3, '0825', 105.459383, 30.346121, 2706);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2711, '510903', '船山区', 3, '0825', 105.582215, 30.502647, 2706);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2712, '512000', '资阳市', 2, '0832', 104.641917, 30.122211, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2713, '512021', '安岳县', 3, '0832', 105.336764, 30.099206, 2712);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2714, '512002', '雁江区', 3, '0832', 104.642338, 30.121686, 2712);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2715, '512022', '乐至县', 3, '0832', 105.031142, 30.275619, 2712);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2716, '510700', '绵阳市', 2, '0816', 104.741722, 31.46402, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2717, '510704', '游仙区', 3, '0816', 104.770006, 31.484772, 2716);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2718, '510725', '梓潼县', 3, '0816', 105.16353, 31.635225, 2716);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2719, '510722', '三台县', 3, '0816', 105.090316, 31.090909, 2716);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2720, '510781', '江油市', 3, '0816', 104.744431, 31.776386, 2716);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2721, '510723', '盐亭县', 3, '0816', 105.391991, 31.22318, 2716);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2722, '510726', '北川羌族自治县', 3, '0816', 104.468069, 31.615863, 2716);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2723, '510705', '安州区', 3, '0816', 104.560341, 31.53894, 2716);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2724, '510703', '涪城区', 3, '0816', 104.740971, 31.463557, 2716);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2725, '510727', '平武县', 3, '0816', 104.530555, 32.407588, 2716);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2726, '511600', '广安市', 2, '0826', 106.633369, 30.456398, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2727, '511623', '邻水县', 3, '0826', 106.934968, 30.334323, 2726);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2728, '511622', '武胜县', 3, '0826', 106.292473, 30.344291, 2726);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2729, '511602', '广安区', 3, '0826', 106.632907, 30.456462, 2726);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2730, '511681', '华蓥市', 3, '0826', 106.777882, 30.380574, 2726);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2731, '511603', '前锋区', 3, '0826', 106.893277, 30.4963, 2726);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2732, '511621', '岳池县', 3, '0826', 106.444451, 30.533538, 2726);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2733, '511700', '达州市', 2, '0818', 107.502262, 31.209484, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2734, '511724', '大竹县', 3, '0818', 107.20742, 30.736289, 2733);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2735, '511725', '渠县', 3, '0818', 106.970746, 30.836348, 2733);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2736, '511722', '宣汉县', 3, '0818', 107.722254, 31.355025, 2733);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2737, '511723', '开江县', 3, '0818', 107.864135, 31.085537, 2733);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2738, '511781', '万源市', 3, '0818', 108.037548, 32.06777, 2733);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2739, '511702', '通川区', 3, '0818', 107.501062, 31.213522, 2733);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2740, '511703', '达川区', 3, '0818', 107.507926, 31.199062, 2733);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2741, '511300', '南充市', 2, '0817', 106.082974, 30.795281, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2742, '511323', '蓬安县', 3, '0817', 106.413488, 31.027978, 2741);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2743, '511325', '西充县', 3, '0817', 105.893021, 30.994616, 2741);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2744, '511322', '营山县', 3, '0817', 106.564893, 31.075907, 2741);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2745, '511321', '南部县', 3, '0817', 106.061138, 31.349407, 2741);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2746, '511304', '嘉陵区', 3, '0817', 106.067027, 30.762976, 2741);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2747, '511324', '仪陇县', 3, '0817', 106.297083, 31.271261, 2741);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2748, '511303', '高坪区', 3, '0817', 106.108996, 30.781809, 2741);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2749, '511302', '顺庆区', 3, '0817', 106.084091, 30.795572, 2741);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2750, '511381', '阆中市', 3, '0817', 105.975266, 31.580466, 2741);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2751, '511000', '内江市', 2, '1832', 105.066138, 29.58708, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2752, '511025', '资中县', 3, '1832', 104.852463, 29.775295, 2751);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2753, '511011', '东兴区', 3, '1832', 105.067203, 29.600107, 2751);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2754, '511024', '威远县', 3, '1832', 104.668327, 29.52686, 2751);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2755, '511083', '隆昌市', 3, '1832', 105.288074, 29.338162, 2751);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2756, '511002', '市中区', 3, '1832', 105.065467, 29.585265, 2751);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2757, '511500', '宜宾市', 2, '0831', 104.630825, 28.760189, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2758, '511526', '珙县', 3, '0831', 104.712268, 28.449041, 2757);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2759, '511528', '兴文县', 3, '0831', 105.236549, 28.302988, 2757);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2760, '511524', '长宁县', 3, '0831', 104.921116, 28.577271, 2757);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2761, '511523', '江安县', 3, '0831', 105.068697, 28.728102, 2757);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2762, '511503', '南溪区', 3, '0831', 104.981133, 28.839806, 2757);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2763, '511502', '翠屏区', 3, '0831', 104.630231, 28.760179, 2757);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2764, '511525', '高县', 3, '0831', 104.519187, 28.435676, 2757);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2765, '511527', '筠连县', 3, '0831', 104.507848, 28.162017, 2757);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2766, '511529', '屏山县', 3, '0831', 104.162617, 28.64237, 2757);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2767, '511504', '叙州区', 3, '0831', 104.541489, 28.695678, 2757);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2768, '510500', '泸州市', 2, '0830', 105.443348, 28.889138, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2769, '510504', '龙马潭区', 3, '0830', 105.435228, 28.897572, 2768);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2770, '510521', '泸县', 3, '0830', 105.376335, 29.151288, 2768);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2771, '510503', '纳溪区', 3, '0830', 105.37721, 28.77631, 2768);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2772, '510524', '叙永县', 3, '0830', 105.437775, 28.167919, 2768);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2773, '510525', '古蔺县', 3, '0830', 105.813359, 28.03948, 2768);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2774, '510502', '江阳区', 3, '0830', 105.445131, 28.882889, 2768);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2775, '510522', '合江县', 3, '0830', 105.834098, 28.810325, 2768);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2776, '513200', '阿坝藏族羌族自治州', 2, '0837', 102.221374, 31.899792, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2777, '513231', '阿坝县', 3, '0837', 101.700985, 32.904223, 2776);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2778, '513228', '黑水县', 3, '0837', 102.990805, 32.061721, 2776);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2779, '513225', '九寨沟县', 3, '0837', 104.236344, 33.262097, 2776);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2780, '513222', '理县', 3, '0837', 103.165486, 31.436764, 2776);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2781, '513232', '若尔盖县', 3, '0837', 102.963726, 33.575934, 2776);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2782, '513226', '金川县', 3, '0837', 102.064647, 31.476356, 2776);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2783, '513233', '红原县', 3, '0837', 102.544906, 32.793902, 2776);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2784, '513221', '汶川县', 3, '0837', 103.580675, 31.47463, 2776);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2785, '513201', '马尔康市', 3, '0837', 102.221187, 31.899761, 2776);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2786, '513224', '松潘县', 3, '0837', 103.599177, 32.63838, 2776);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2787, '513223', '茂县', 3, '0837', 103.850684, 31.680407, 2776);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2788, '513227', '小金县', 3, '0837', 102.363193, 30.999016, 2776);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2789, '513230', '壤塘县', 3, '0837', 100.979136, 32.264887, 2776);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2790, '510300', '自贡市', 2, '0813', 104.773447, 29.352765, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2791, '510311', '沿滩区', 3, '0813', 104.876417, 29.272521, 2790);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2792, '510304', '大安区', 3, '0813', 104.783229, 29.367136, 2790);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2793, '510321', '荣县', 3, '0813', 104.423932, 29.454851, 2790);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2794, '510302', '自流井区', 3, '0813', 104.778188, 29.343231, 2790);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2795, '510322', '富顺县', 3, '0813', 104.984256, 29.181282, 2790);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2796, '510303', '贡井区', 3, '0813', 104.714372, 29.345675, 2790);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2797, '511800', '雅安市', 2, '0835', 103.001033, 29.987722, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2798, '511827', '宝兴县', 3, '0835', 102.813377, 30.369026, 2797);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2799, '511822', '荥经县', 3, '0835', 102.844674, 29.795529, 2797);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2800, '511824', '石棉县', 3, '0835', 102.35962, 29.234063, 2797);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2801, '511823', '汉源县', 3, '0835', 102.677145, 29.349915, 2797);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2802, '511825', '天全县', 3, '0835', 102.763462, 30.059955, 2797);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2803, '511802', '雨城区', 3, '0835', 103.003398, 29.981831, 2797);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2804, '511826', '芦山县', 3, '0835', 102.924016, 30.152907, 2797);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2805, '511803', '名山区', 3, '0835', 103.112214, 30.084718, 2797);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2806, '511400', '眉山市', 2, '1833', 103.831788, 30.048318, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2807, '511423', '洪雅县', 3, '1833', 103.375006, 29.904867, 2806);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2808, '511424', '丹棱县', 3, '1833', 103.518333, 30.012751, 2806);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2809, '511421', '仁寿县', 3, '1833', 104.147646, 29.996721, 2806);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2810, '511403', '彭山区', 3, '1833', 103.8701, 30.192298, 2806);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2811, '511425', '青神县', 3, '1833', 103.846131, 29.831469, 2806);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2812, '511402', '东坡区', 3, '1833', 103.831553, 30.048128, 2806);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2813, '511100', '乐山市', 2, '0833', 103.761263, 29.582024, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2814, '511111', '沙湾区', 3, '0833', 103.549961, 29.416536, 2813);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2815, '511126', '夹江县', 3, '0833', 103.578862, 29.741019, 2813);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2816, '511124', '井研县', 3, '0833', 104.06885, 29.651645, 2813);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2817, '511113', '金口河区', 3, '0833', 103.077831, 29.24602, 2813);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2818, '511123', '犍为县', 3, '0833', 103.944266, 29.209782, 2813);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2819, '511112', '五通桥区', 3, '0833', 103.816837, 29.406186, 2813);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2820, '511129', '沐川县', 3, '0833', 103.90211, 28.956338, 2813);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2821, '511132', '峨边彝族自治县', 3, '0833', 103.262148, 29.230271, 2813);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2822, '511133', '马边彝族自治县', 3, '0833', 103.546851, 28.838933, 2813);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2823, '511102', '市中区', 3, '0833', 103.75539, 29.588327, 2813);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2824, '511181', '峨眉山市', 3, '0833', 103.492488, 29.597478, 2813);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2825, '510400', '攀枝花市', 2, '0812', 101.716007, 26.580446, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2826, '510421', '米易县', 3, '0812', 102.109877, 26.887474, 2825);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2827, '510403', '西区', 3, '0812', 101.637969, 26.596776, 2825);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2828, '510422', '盐边县', 3, '0812', 101.851848, 26.677619, 2825);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2829, '510411', '仁和区', 3, '0812', 101.737916, 26.497185, 2825);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2830, '510402', '东区', 3, '0812', 101.715134, 26.580887, 2825);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2831, '513400', '凉山彝族自治州', 2, '0834', 102.258746, 27.886762, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2832, '513425', '会理县', 3, '0834', 102.249548, 26.658702, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2833, '513437', '雷波县', 3, '0834', 103.571584, 28.262946, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2834, '513432', '喜德县', 3, '0834', 102.412342, 28.305486, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2835, '513431', '昭觉县', 3, '0834', 102.843991, 28.010554, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2836, '513429', '布拖县', 3, '0834', 102.808801, 27.709062, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2837, '513401', '西昌市', 3, '0834', 102.258758, 27.885786, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2838, '513428', '普格县', 3, '0834', 102.541082, 27.376828, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2839, '513422', '木里藏族自治县', 3, '0834', 101.280184, 27.926859, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2840, '513433', '冕宁县', 3, '0834', 102.170046, 28.550844, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2841, '513426', '会东县', 3, '0834', 102.578985, 26.630713, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2842, '513434', '越西县', 3, '0834', 102.508875, 28.639632, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2843, '513435', '甘洛县', 3, '0834', 102.775924, 28.977094, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2844, '513436', '美姑县', 3, '0834', 103.132007, 28.327946, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2845, '513423', '盐源县', 3, '0834', 101.508909, 27.423415, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2846, '513430', '金阳县', 3, '0834', 103.248704, 27.695916, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2847, '513424', '德昌县', 3, '0834', 102.178845, 27.403827, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2848, '513427', '宁南县', 3, '0834', 102.757374, 27.065205, 2831);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2849, '513300', '甘孜藏族自治州', 2, '0836', 101.963815, 30.050663, 2663);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2850, '513328', '甘孜县', 3, '0836', 99.991753, 31.61975, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2851, '513332', '石渠县', 3, '0836', 98.100887, 32.975302, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2852, '513337', '稻城县', 3, '0836', 100.296689, 29.037544, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2853, '513329', '新龙县', 3, '0836', 100.312094, 30.93896, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2854, '513330', '德格县', 3, '0836', 98.57999, 31.806729, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2855, '513325', '雅江县', 3, '0836', 101.015735, 30.03225, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2856, '513331', '白玉县', 3, '0836', 98.824343, 31.208805, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2857, '513326', '道孚县', 3, '0836', 101.123327, 30.978767, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2858, '513335', '巴塘县', 3, '0836', 99.109037, 30.005723, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2859, '513327', '炉霍县', 3, '0836', 100.679495, 31.392674, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2860, '513301', '康定市', 3, '0836', 101.964057, 30.050738, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2861, '513334', '理塘县', 3, '0836', 100.269862, 29.991807, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2862, '513336', '乡城县', 3, '0836', 99.799943, 28.930855, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2863, '513324', '九龙县', 3, '0836', 101.506942, 29.001975, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2864, '513338', '得荣县', 3, '0836', 99.288036, 28.71134, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2865, '513333', '色达县', 3, '0836', 100.331657, 32.268777, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2866, '513323', '丹巴县', 3, '0836', 101.886125, 30.877083, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2867, '513322', '泸定县', 3, '0836', 102.233225, 29.912482, 2849);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2868, '220000', '吉林省', 1, '', 125.3245, 43.886841, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2869, '220800', '白城市', 2, '0436', 122.841114, 45.619026, 2868);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2870, '220881', '洮南市', 3, '0436', 122.783779, 45.339113, 2869);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2871, '220882', '大安市', 3, '0436', 124.291512, 45.507648, 2869);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2872, '220822', '通榆县', 3, '0436', 123.088543, 44.80915, 2869);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2873, '220802', '洮北区', 3, '0436', 122.842499, 45.619253, 2869);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2874, '220821', '镇赉县', 3, '0436', 123.202246, 45.846089, 2869);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2875, '220100', '长春市', 2, '0431', 125.3245, 43.886841, 2868);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2876, '220183', '德惠市', 3, '0431', 125.703327, 44.533909, 2875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2877, '220182', '榆树市', 3, '0431', 126.550107, 44.827642, 2875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2878, '220112', '双阳区', 3, '0431', 125.659018, 43.525168, 2875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2879, '220122', '农安县', 3, '0431', 125.175287, 44.431258, 2875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2880, '220103', '宽城区', 3, '0431', 125.342828, 43.903823, 2875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2881, '220113', '九台区', 3, '0431', 125.844682, 44.157155, 2875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2882, '220106', '绿园区', 3, '0431', 125.272467, 43.892177, 2875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2883, '220184', '公主岭市', 3, '0431', 124.817588, 43.509474, 2875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2884, '220104', '朝阳区', 3, '0431', 125.318042, 43.86491, 2875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2885, '220105', '二道区', 3, '0431', 125.384727, 43.870824, 2875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2886, '220102', '南关区', 3, '0431', 125.337237, 43.890235, 2875);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2887, '220200', '吉林市', 2, '0432', 126.55302, 43.843577, 2868);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2888, '220211', '丰满区', 3, '0432', 126.560759, 43.816594, 2887);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2889, '220283', '舒兰市', 3, '0432', 126.947813, 44.410906, 2887);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2890, '220282', '桦甸市', 3, '0432', 126.745445, 42.972093, 2887);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2891, '220281', '蛟河市', 3, '0432', 127.342739, 43.720579, 2887);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2892, '220203', '龙潭区', 3, '0432', 126.561429, 43.909755, 2887);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2893, '220221', '永吉县', 3, '0432', 126.501622, 43.667416, 2887);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2894, '220284', '磐石市', 3, '0432', 126.059929, 42.942476, 2887);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2895, '220202', '昌邑区', 3, '0432', 126.570766, 43.851118, 2887);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2896, '220204', '船营区', 3, '0432', 126.55239, 43.843804, 2887);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2897, '220700', '松原市', 2, '0438', 124.823608, 45.118243, 2868);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2898, '220781', '扶余市', 3, '0438', 126.042758, 44.986199, 2897);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2899, '220702', '宁江区', 3, '0438', 124.827851, 45.176498, 2897);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2900, '220723', '乾安县', 3, '0438', 124.024361, 45.006846, 2897);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2901, '220721', '前郭尔罗斯蒙古族自治县', 3, '0438', 124.826808, 45.116288, 2897);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2902, '220722', '长岭县', 3, '0438', 123.985184, 44.276579, 2897);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2903, '220400', '辽源市', 2, '0437', 125.145349, 42.902692, 2868);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2904, '220403', '西安区', 3, '0437', 125.151424, 42.920415, 2903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2905, '220421', '东丰县', 3, '0437', 125.529623, 42.675228, 2903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2906, '220422', '东辽县', 3, '0437', 124.991995, 42.927724, 2903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2907, '220402', '龙山区', 3, '0437', 125.145164, 42.902702, 2903);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2908, '222400', '延边朝鲜族自治州', 2, '1433', 129.513228, 42.904823, 2868);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2909, '222403', '敦化市', 3, '1433', 128.22986, 43.366921, 2908);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2910, '222404', '珲春市', 3, '1433', 130.365787, 42.871057, 2908);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2911, '222402', '图们市', 3, '1433', 129.846701, 42.966621, 2908);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2912, '222406', '和龙市', 3, '1433', 129.008748, 42.547004, 2908);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2913, '222424', '汪清县', 3, '1433', 129.766161, 43.315426, 2908);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2914, '222401', '延吉市', 3, '1433', 129.51579, 42.906964, 2908);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2915, '222426', '安图县', 3, '1433', 128.901865, 43.110994, 2908);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2916, '222405', '龙井市', 3, '1433', 129.425747, 42.771029, 2908);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2917, '220600', '白山市', 2, '0439', 126.427839, 41.942505, 2868);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2918, '220623', '长白朝鲜族自治县', 3, '0439', 128.203384, 41.419361, 2917);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2919, '220605', '江源区', 3, '0439', 126.584229, 42.048109, 2917);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2920, '220622', '靖宇县', 3, '0439', 126.808386, 42.389689, 2917);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2921, '220621', '抚松县', 3, '0439', 127.273796, 42.332643, 2917);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2922, '220602', '浑江区', 3, '0439', 126.428035, 41.943065, 2917);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2923, '220681', '临江市', 3, '0439', 126.919296, 41.810689, 2917);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2924, '220500', '通化市', 2, '0435', 125.936501, 41.721177, 2868);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2925, '220521', '通化县', 3, '0435', 125.753121, 41.677918, 2924);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2926, '220503', '二道江区', 3, '0435', 126.045987, 41.777564, 2924);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2927, '220502', '东昌区', 3, '0435', 125.936716, 41.721233, 2924);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2928, '220524', '柳河县', 3, '0435', 125.740536, 42.281484, 2924);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2929, '220581', '梅河口市', 3, '0435', 125.687336, 42.530002, 2924);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2930, '220523', '辉南县', 3, '0435', 126.042821, 42.683459, 2924);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2931, '220582', '集安市', 3, '0435', 126.186204, 41.126276, 2924);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2932, '220300', '四平市', 2, '0434', 124.370785, 43.170344, 2868);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2933, '220303', '铁东区', 3, '0434', 124.388464, 43.16726, 2932);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2934, '220382', '双辽市', 3, '0434', 123.505283, 43.518275, 2932);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2935, '220323', '伊通满族自治县', 3, '0434', 125.303124, 43.345464, 2932);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2936, '220322', '梨树县', 3, '0434', 124.335802, 43.30831, 2932);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2937, '220302', '铁西区', 3, '0434', 124.360894, 43.176263, 2932);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2938, '120000', '天津市', 1, '022', 117.190182, 39.125596, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2939, '120100', '天津城区', 2, '022', 117.190182, 39.125596, 2938);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2940, '120102', '河东区', 3, '022', 117.226568, 39.122125, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2941, '120105', '河北区', 3, '022', 117.201569, 39.156632, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2942, '120101', '和平区', 3, '022', 117.195907, 39.118327, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2943, '120115', '宝坻区', 3, '022', 117.308094, 39.716965, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2944, '120110', '东丽区', 3, '022', 117.313967, 39.087764, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2945, '120112', '津南区', 3, '022', 117.382549, 38.989577, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2946, '120116', '滨海新区', 3, '022', 117.654173, 39.032846, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2947, '120118', '静海区', 3, '022', 116.925304, 38.935671, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2948, '120119', '蓟州区', 3, '022', 117.407449, 40.045342, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2949, '120114', '武清区', 3, '022', 117.057959, 39.376925, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2950, '120104', '南开区', 3, '022', 117.164143, 39.120474, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2951, '120103', '河西区', 3, '022', 117.217536, 39.101897, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2952, '120117', '宁河区', 3, '022', 117.82828, 39.328886, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2953, '120106', '红桥区', 3, '022', 117.163301, 39.175066, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2954, '120113', '北辰区', 3, '022', 117.13482, 39.225555, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2955, '120111', '西青区', 3, '022', 117.012247, 39.139446, 2939);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2956, '140000', '山西省', 1, '', 112.549248, 37.857014, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2957, '140800', '运城市', 2, '0359', 111.003957, 35.022778, 2956);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2958, '140822', '万荣县', 3, '0359', 110.843561, 35.417042, 2957);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2959, '140821', '临猗县', 3, '0359', 110.77493, 35.141883, 2957);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2960, '140823', '闻喜县', 3, '0359', 111.220306, 35.353839, 2957);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2961, '140802', '盐湖区', 3, '0359', 111.000627, 35.025643, 2957);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2962, '140830', '芮城县', 3, '0359', 110.69114, 34.694769, 2957);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2963, '140828', '夏县', 3, '0359', 111.223174, 35.140441, 2957);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2964, '140829', '平陆县', 3, '0359', 111.212377, 34.837256, 2957);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2965, '140881', '永济市', 3, '0359', 110.447984, 34.865125, 2957);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2966, '140826', '绛县', 3, '0359', 111.576182, 35.49045, 2957);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2967, '140827', '垣曲县', 3, '0359', 111.67099, 35.298293, 2957);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2968, '140825', '新绛县', 3, '0359', 111.225205, 35.613697, 2957);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2969, '140824', '稷山县', 3, '0359', 110.978996, 35.600412, 2957);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2970, '140882', '河津市', 3, '0359', 110.710268, 35.59715, 2957);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2971, '141000', '临汾市', 2, '0357', 111.517973, 36.08415, 2956);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2972, '141025', '古县', 3, '0357', 111.920207, 36.26855, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2973, '141026', '安泽县', 3, '0357', 112.251372, 36.146032, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2974, '141030', '大宁县', 3, '0357', 110.751283, 36.46383, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2975, '141034', '汾西县', 3, '0357', 111.563021, 36.653368, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2976, '141031', '隰县', 3, '0357', 110.935809, 36.692675, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2977, '141028', '吉县', 3, '0357', 110.682853, 36.099355, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2978, '141081', '侯马市', 3, '0357', 111.371272, 35.620302, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2979, '141032', '永和县', 3, '0357', 110.631276, 36.760614, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2980, '141082', '霍州市', 3, '0357', 111.723103, 36.57202, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2981, '141029', '乡宁县', 3, '0357', 110.857365, 35.975402, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2982, '141021', '曲沃县', 3, '0357', 111.475529, 35.641387, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2983, '141033', '蒲县', 3, '0357', 111.09733, 36.411682, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2984, '141027', '浮山县', 3, '0357', 111.850039, 35.971359, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2985, '141023', '襄汾县', 3, '0357', 111.442932, 35.876139, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2986, '141024', '洪洞县', 3, '0357', 111.673692, 36.255742, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2987, '141002', '尧都区', 3, '0357', 111.522945, 36.080366, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2988, '141022', '翼城县', 3, '0357', 111.713508, 35.738621, 2971);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2989, '140100', '太原市', 2, '0351', 112.549248, 37.857014, 2956);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2990, '140109', '万柏林区', 3, '0351', 112.522258, 37.862653, 2989);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2991, '140110', '晋源区', 3, '0351', 112.477849, 37.715619, 2989);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2992, '140108', '尖草坪区', 3, '0351', 112.487122, 37.939893, 2989);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2993, '140123', '娄烦县', 3, '0351', 111.793798, 38.066035, 2989);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2994, '140107', '杏花岭区', 3, '0351', 112.560743, 37.879291, 2989);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2995, '140106', '迎泽区', 3, '0351', 112.558851, 37.855804, 2989);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2996, '140122', '阳曲县', 3, '0351', 112.673818, 38.058797, 2989);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2997, '140121', '清徐县', 3, '0351', 112.357961, 37.60729, 2989);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2998, '140105', '小店区', 3, '0351', 112.564273, 37.817974, 2989);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (2999, '140181', '古交市', 3, '0351', 112.174353, 37.908534, 2989);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3000, '140300', '阳泉市', 2, '0353', 113.583285, 37.861188, 2956);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3001, '140321', '平定县', 3, '0353', 113.631049, 37.800289, 3000);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3002, '140311', '郊区', 3, '0353', 113.58664, 37.94096, 3000);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3003, '140303', '矿区', 3, '0353', 113.559066, 37.870085, 3000);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3004, '140322', '盂县', 3, '0353', 113.41223, 38.086131, 3000);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3005, '140302', '城区', 3, '0353', 113.586513, 37.860938, 3000);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3006, '140600', '朔州市', 2, '0349', 112.433387, 39.331261, 2956);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3007, '140603', '平鲁区', 3, '0349', 112.295227, 39.515603, 3006);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3008, '140623', '右玉县', 3, '0349', 112.465588, 39.988812, 3006);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3009, '140602', '朔城区', 3, '0349', 112.428676, 39.324525, 3006);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3010, '140622', '应县', 3, '0349', 113.187505, 39.559187, 3006);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3011, '140621', '山阴县', 3, '0349', 112.816396, 39.52677, 3006);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3012, '140681', '怀仁市', 3, '0349', 113.100511, 39.820789, 3006);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3013, '140900', '忻州市', 2, '0350', 112.733538, 38.41769, 2956);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3014, '140932', '偏关县', 3, '0350', 111.500477, 39.442153, 3013);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3015, '140926', '静乐县', 3, '0350', 111.940231, 38.355947, 3013);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3016, '140981', '原平市', 3, '0350', 112.713132, 38.729186, 3013);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3017, '140929', '岢岚县', 3, '0350', 111.56981, 38.705625, 3013);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3018, '140923', '代县', 3, '0350', 112.962519, 39.065138, 3013);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3019, '140928', '五寨县', 3, '0350', 111.841015, 38.912761, 3013);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3020, '140925', '宁武县', 3, '0350', 112.307936, 39.001718, 3013);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3021, '140930', '河曲县', 3, '0350', 111.146609, 39.381895, 3013);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3022, '140922', '五台县', 3, '0350', 113.259012, 38.725711, 3013);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3023, '140921', '定襄县', 3, '0350', 112.963231, 38.484948, 3013);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3024, '140902', '忻府区', 3, '0350', 112.734112, 38.417743, 3013);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3025, '140931', '保德县', 3, '0350', 111.085688, 39.022576, 3013);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3026, '140927', '神池县', 3, '0350', 112.200438, 39.088467, 3013);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3027, '140924', '繁峙县', 3, '0350', 113.267707, 39.188104, 3013);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3028, '140700', '晋中市', 2, '0354', 112.736465, 37.696495, 2956);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3029, '140724', '昔阳县', 3, '0354', 113.706166, 37.60437, 3028);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3030, '140723', '和顺县', 3, '0354', 113.572919, 37.327027, 3028);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3031, '140725', '寿阳县', 3, '0354', 113.177708, 37.891136, 3028);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3032, '140722', '左权县', 3, '0354', 113.377834, 37.079672, 3028);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3033, '140721', '榆社县', 3, '0354', 112.973521, 37.069019, 3028);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3034, '140781', '介休市', 3, '0354', 111.913857, 37.027616, 3028);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3035, '140729', '灵石县', 3, '0354', 111.772759, 36.847469, 3028);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3036, '140702', '榆次区', 3, '0354', 112.740056, 37.6976, 3028);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3037, '140727', '祁县', 3, '0354', 112.330532, 37.358739, 3028);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3038, '140703', '太谷区', 3, '0354', 112.554103, 37.424595, 3028);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3039, '140728', '平遥县', 3, '0354', 112.174059, 37.195474, 3028);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3040, '141100', '吕梁市', 2, '0358', 111.134335, 37.524366, 2956);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3041, '141130', '交口县', 3, '0358', 111.183188, 36.983068, 3040);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3042, '141128', '方山县', 3, '0358', 111.238885, 37.892632, 3040);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3043, '141127', '岚县', 3, '0358', 111.671555, 38.278654, 3040);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3044, '141124', '临县', 3, '0358', 110.995963, 37.960806, 3040);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3045, '141102', '离石区', 3, '0358', 111.134462, 37.524037, 3040);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3046, '141125', '柳林县', 3, '0358', 110.89613, 37.431664, 3040);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3047, '141129', '中阳县', 3, '0358', 111.193319, 37.342054, 3040);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3048, '141123', '兴县', 3, '0358', 111.124816, 38.464136, 3040);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3049, '141126', '石楼县', 3, '0358', 110.837119, 36.999426, 3040);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3050, '141122', '交城县', 3, '0358', 112.159154, 37.555155, 3040);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3051, '141121', '文水县', 3, '0358', 112.032595, 37.436314, 3040);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3052, '141182', '汾阳市', 3, '0358', 111.785273, 37.267742, 3040);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3053, '141181', '孝义市', 3, '0358', 111.781568, 37.144474, 3040);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3054, '140200', '大同市', 2, '0352', 113.295259, 40.09031, 2956);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3055, '140225', '浑源县', 3, '0352', 113.698091, 39.699099, 3054);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3056, '140224', '灵丘县', 3, '0352', 114.23576, 39.438867, 3054);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3057, '140223', '广灵县', 3, '0352', 114.279252, 39.763051, 3054);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3058, '140221', '阳高县', 3, '0352', 113.749871, 40.364927, 3054);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3059, '140215', '云州区', 3, '0352', 113.61244, 40.040295, 3054);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3060, '140214', '云冈区', 3, '0352', 113.149693, 40.005405, 3054);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3061, '140213', '平城区', 3, '0352', 113.298027, 40.075667, 3054);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3062, '140222', '天镇县', 3, '0352', 114.09112, 40.421336, 3054);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3063, '140212', '新荣区', 3, '0352', 113.141044, 40.258269, 3054);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3064, '140226', '左云县', 3, '0352', 112.70641, 40.012873, 3054);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3065, '140400', '长治市', 2, '0355', 113.113556, 36.191112, 2956);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3066, '140406', '潞城区', 3, '0355', 113.223245, 36.332232, 3065);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3067, '140428', '长子县', 3, '0355', 112.884656, 36.119484, 3065);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3068, '140426', '黎城县', 3, '0355', 113.387366, 36.502971, 3065);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3069, '140404', '上党区', 3, '0355', 113.056679, 36.052438, 3065);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3070, '140405', '屯留区', 3, '0355', 112.892741, 36.314072, 3065);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3071, '140431', '沁源县', 3, '0355', 112.340878, 36.500777, 3065);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3072, '140425', '平顺县', 3, '0355', 113.438791, 36.200202, 3065);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3073, '140423', '襄垣县', 3, '0355', 113.050094, 36.532854, 3065);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3074, '140403', '潞州区', 3, '0355', 113.114107, 36.187895, 3065);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3075, '140430', '沁县', 3, '0355', 112.70138, 36.757123, 3065);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3076, '140429', '武乡县', 3, '0355', 112.8653, 36.834315, 3065);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3077, '140427', '壶关县', 3, '0355', 113.206138, 36.110938, 3065);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3078, '140500', '晋城市', 2, '0356', 112.851274, 35.497553, 2956);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3079, '140581', '高平市', 3, '0356', 112.930691, 35.791355, 3078);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3080, '140502', '城区', 3, '0356', 112.853106, 35.496641, 3078);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3081, '140525', '泽州县', 3, '0356', 112.899137, 35.617221, 3078);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3082, '140521', '沁水县', 3, '0356', 112.187213, 35.689472, 3078);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3083, '140522', '阳城县', 3, '0356', 112.422014, 35.482177, 3078);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3084, '140524', '陵川县', 3, '0356', 113.278877, 35.775614, 3078);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3085, '530000', '云南省', 1, '', 102.712251, 25.040609, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3086, '530600', '昭通市', 2, '0870', 103.717216, 27.336999, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3087, '530681', '水富市', 3, '0870', 104.415376, 28.629688, 3086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3088, '530626', '绥江县', 3, '0870', 103.961095, 28.599953, 3086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3089, '530629', '威信县', 3, '0870', 105.04869, 27.843381, 3086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3090, '530625', '永善县', 3, '0870', 103.63732, 28.231526, 3086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3091, '530624', '大关县', 3, '0870', 103.891608, 27.747114, 3086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3092, '530622', '巧家县', 3, '0870', 102.929284, 26.9117, 3086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3093, '530602', '昭阳区', 3, '0870', 103.717267, 27.336636, 3086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3094, '530623', '盐津县', 3, '0870', 104.23506, 28.106923, 3086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3095, '530621', '鲁甸县', 3, '0870', 103.549333, 27.191637, 3086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3096, '530628', '彝良县', 3, '0870', 104.048492, 27.627425, 3086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3097, '530627', '镇雄县', 3, '0870', 104.873055, 27.436267, 3086);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3098, '532500', '红河哈尼族彝族自治州', 2, '0873', 103.384182, 23.366775, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3099, '532527', '泸西县', 3, '0873', 103.759622, 24.532368, 3098);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3100, '532504', '弥勒市', 3, '0873', 103.436988, 24.40837, 3098);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3101, '532524', '建水县', 3, '0873', 102.820493, 23.618387, 3098);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3102, '532525', '石屏县', 3, '0873', 102.484469, 23.712569, 3098);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3103, '532502', '开远市', 3, '0873', 103.258679, 23.713832, 3098);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3104, '532501', '个旧市', 3, '0873', 103.154752, 23.360383, 3098);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3105, '532503', '蒙自市', 3, '0873', 103.385005, 23.366843, 3098);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3106, '532529', '红河县', 3, '0873', 102.42121, 23.369191, 3098);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3107, '532523', '屏边苗族自治县', 3, '0873', 103.687229, 22.987013, 3098);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3108, '532531', '绿春县', 3, '0873', 102.39286, 22.99352, 3098);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3109, '532528', '元阳县', 3, '0873', 102.837056, 23.219773, 3098);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3110, '532530', '金平苗族瑶族傣族自治县', 3, '0873', 103.228359, 22.779982, 3098);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3111, '532532', '河口瑶族自治县', 3, '0873', 103.961593, 22.507563, 3098);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3112, '530300', '曲靖市', 2, '0874', 103.797851, 25.501557, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3113, '530326', '会泽县', 3, '0874', 103.300041, 26.412861, 3112);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3114, '530304', '马龙区', 3, '0874', 103.578755, 25.429451, 3112);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3115, '530324', '罗平县', 3, '0874', 104.309263, 24.885708, 3112);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3116, '530323', '师宗县', 3, '0874', 103.993808, 24.825681, 3112);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3117, '530322', '陆良县', 3, '0874', 103.655233, 25.022878, 3112);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3118, '530381', '宣威市', 3, '0874', 104.09554, 26.227777, 3112);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3119, '530325', '富源县', 3, '0874', 104.25692, 25.67064, 3112);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3120, '530302', '麒麟区', 3, '0874', 103.798054, 25.501269, 3112);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3121, '530303', '沾益区', 3, '0874', 103.819262, 25.600878, 3112);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3122, '533300', '怒江傈僳族自治州', 2, '0886', 98.854304, 25.850949, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3123, '533324', '贡山独龙族怒族自治县', 3, '0886', 98.666141, 27.738054, 3122);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3124, '533323', '福贡县', 3, '0886', 98.867413, 26.902738, 3122);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3125, '533325', '兰坪白族普米族自治县', 3, '0886', 99.421378, 26.453839, 3122);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3126, '533301', '泸水市', 3, '0886', 98.854063, 25.851142, 3122);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3127, '530400', '玉溪市', 2, '0877', 102.543907, 24.350461, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3128, '530425', '易门县', 3, '0877', 102.16211, 24.669598, 3127);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3129, '530481', '澄江市', 3, '0877', 102.916652, 24.669679, 3127);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3130, '530426', '峨山彝族自治县', 3, '0877', 102.404358, 24.173256, 3127);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3131, '530424', '华宁县', 3, '0877', 102.928982, 24.189807, 3127);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3132, '530427', '新平彝族傣族自治县', 3, '0877', 101.990903, 24.0664, 3127);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3133, '530403', '江川区', 3, '0877', 102.749839, 24.291006, 3127);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3134, '530402', '红塔区', 3, '0877', 102.543468, 24.350753, 3127);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3135, '530423', '通海县', 3, '0877', 102.760039, 24.112205, 3127);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3136, '530428', '元江哈尼族彝族傣族自治县', 3, '0877', 101.999658, 23.597618, 3127);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3137, '530500', '保山市', 2, '0875', 99.167133, 25.111802, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3138, '530581', '腾冲市', 3, '0875', 98.497292, 25.01757, 3137);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3139, '530502', '隆阳区', 3, '0875', 99.165825, 25.112144, 3137);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3140, '530524', '昌宁县', 3, '0875', 99.612344, 24.823662, 3137);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3141, '530521', '施甸县', 3, '0875', 99.183758, 24.730847, 3137);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3142, '530523', '龙陵县', 3, '0875', 98.693567, 24.591912, 3137);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3143, '532900', '大理白族自治州', 2, '0872', 100.225668, 25.589449, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3144, '532931', '剑川县', 3, '0872', 99.905887, 26.530066, 3143);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3145, '532932', '鹤庆县', 3, '0872', 100.173375, 26.55839, 3143);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3146, '532930', '洱源县', 3, '0872', 99.951708, 26.111184, 3143);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3147, '532924', '宾川县', 3, '0872', 100.578957, 25.825904, 3143);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3148, '532929', '云龙县', 3, '0872', 99.369402, 25.884955, 3143);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3149, '532901', '大理市', 3, '0872', 100.241369, 25.593067, 3143);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3150, '532923', '祥云县', 3, '0872', 100.554025, 25.477072, 3143);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3151, '532922', '漾濞彝族自治县', 3, '0872', 99.95797, 25.669543, 3143);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3152, '532928', '永平县', 3, '0872', 99.533536, 25.461281, 3143);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3153, '532927', '巍山彝族回族自治县', 3, '0872', 100.30793, 25.230909, 3143);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3154, '532925', '弥渡县', 3, '0872', 100.490669, 25.342594, 3143);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3155, '532926', '南涧彝族自治县', 3, '0872', 100.518683, 25.041279, 3143);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3156, '530700', '丽江市', 2, '0888', 100.233026, 26.872108, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3157, '530721', '玉龙纳西族自治县', 3, '0888', 100.238312, 26.830593, 3156);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3158, '530702', '古城区', 3, '0888', 100.234412, 26.872229, 3156);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3159, '530724', '宁蒗彝族自治县', 3, '0888', 100.852427, 27.281109, 3156);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3160, '530722', '永胜县', 3, '0888', 100.750901, 26.685623, 3156);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3161, '530723', '华坪县', 3, '0888', 101.267796, 26.628834, 3156);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3162, '533400', '迪庆藏族自治州', 2, '0887', 99.706463, 27.826853, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3163, '533422', '德钦县', 3, '0887', 98.91506, 28.483272, 3162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3164, '533401', '香格里拉市', 3, '0887', 99.708667, 27.825804, 3162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3165, '533423', '维西傈僳族自治县', 3, '0887', 99.286355, 27.180948, 3162);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3166, '532800', '西双版纳傣族自治州', 2, '0691', 100.797941, 22.001724, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3167, '532801', '景洪市', 3, '0691', 100.797947, 22.002087, 3166);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3168, '532822', '勐海县', 3, '0691', 100.448288, 21.955866, 3166);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3169, '532823', '勐腊县', 3, '0691', 101.567051, 21.479449, 3166);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3170, '530100', '昆明市', 2, '0871', 102.712251, 25.040609, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3171, '530113', '东川区', 3, '0871', 103.182, 26.08349, 3170);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3172, '530129', '寻甸回族彝族自治县', 3, '0871', 103.257588, 25.559474, 3170);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3173, '530102', '五华区', 3, '0871', 102.704412, 25.042165, 3170);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3174, '530112', '西山区', 3, '0871', 102.705904, 25.02436, 3170);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3175, '530125', '宜良县', 3, '0871', 103.145989, 24.918215, 3170);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3176, '530126', '石林彝族自治县', 3, '0871', 103.271962, 24.754545, 3170);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3177, '530114', '呈贡区', 3, '0871', 102.801382, 24.889275, 3170);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3178, '530115', '晋宁区', 3, '0871', 102.594987, 24.666944, 3170);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3179, '530181', '安宁市', 3, '0871', 102.485544, 24.921785, 3170);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3180, '530124', '富民县', 3, '0871', 102.497888, 25.219667, 3170);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3181, '530128', '禄劝彝族苗族自治县', 3, '0871', 102.46905, 25.556533, 3170);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3182, '530111', '官渡区', 3, '0871', 102.723437, 25.021211, 3170);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3183, '530103', '盘龙区', 3, '0871', 102.729044, 25.070239, 3170);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3184, '530127', '嵩明县', 3, '0871', 103.038777, 25.335087, 3170);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3185, '530800', '普洱市', 2, '0879', 100.972344, 22.777321, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3186, '530823', '景东彝族自治县', 3, '0879', 100.840011, 24.448523, 3185);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3187, '530825', '镇沅彝族哈尼族拉祜族自治县', 3, '0879', 101.108512, 24.005712, 3185);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3188, '530822', '墨江哈尼族自治县', 3, '0879', 101.687606, 23.428165, 3185);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3189, '530824', '景谷傣族彝族自治县', 3, '0879', 100.701425, 23.500278, 3185);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3190, '530821', '宁洱哈尼族彝族自治县', 3, '0879', 101.04524, 23.062507, 3185);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3191, '530802', '思茅区', 3, '0879', 100.973227, 22.776595, 3185);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3192, '530829', '西盟佤族自治县', 3, '0879', 99.594372, 22.644423, 3185);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3193, '530828', '澜沧拉祜族自治县', 3, '0879', 99.931201, 22.553083, 3185);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3194, '530826', '江城哈尼族彝族自治县', 3, '0879', 101.859144, 22.58336, 3185);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3195, '530827', '孟连傣族拉祜族佤族自治县', 3, '0879', 99.585406, 22.325924, 3185);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3196, '532600', '文山壮族苗族自治州', 2, '0876', 104.24401, 23.36951, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3197, '532626', '丘北县', 3, '0876', 104.194366, 24.040982, 3196);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3198, '532627', '广南县', 3, '0876', 105.056684, 24.050272, 3196);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3199, '532622', '砚山县', 3, '0876', 104.343989, 23.612301, 3196);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3200, '532623', '西畴县', 3, '0876', 104.675711, 23.437439, 3196);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3201, '532601', '文山市', 3, '0876', 104.244277, 23.369216, 3196);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3202, '532624', '麻栗坡县', 3, '0876', 104.701899, 23.124202, 3196);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3203, '532625', '马关县', 3, '0876', 104.398619, 23.011723, 3196);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3204, '532628', '富宁县', 3, '0876', 105.62856, 23.626494, 3196);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3205, '532300', '楚雄彝族自治州', 2, '0878', 101.546046, 25.041988, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3206, '532326', '大姚县', 3, '0878', 101.323602, 25.722348, 3205);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3207, '532328', '元谋县', 3, '0878', 101.870837, 25.703313, 3205);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3208, '532325', '姚安县', 3, '0878', 101.238399, 25.505403, 3205);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3209, '532323', '牟定县', 3, '0878', 101.543044, 25.312111, 3205);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3210, '532324', '南华县', 3, '0878', 101.274991, 25.192408, 3205);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3211, '532331', '禄丰县', 3, '0878', 102.075694, 25.14327, 3205);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3212, '532301', '楚雄市', 3, '0878', 101.546145, 25.040912, 3205);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3213, '532322', '双柏县', 3, '0878', 101.63824, 24.685094, 3205);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3214, '532329', '武定县', 3, '0878', 102.406785, 25.5301, 3205);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3215, '532327', '永仁县', 3, '0878', 101.671175, 26.056316, 3205);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3216, '530900', '临沧市', 2, '0883', 100.08697, 23.886567, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3217, '530921', '凤庆县', 3, '0883', 99.91871, 24.592738, 3216);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3218, '530922', '云县', 3, '0883', 100.125637, 24.439026, 3216);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3219, '530923', '永德县', 3, '0883', 99.253679, 24.028159, 3216);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3220, '530924', '镇康县', 3, '0883', 98.82743, 23.761415, 3216);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3221, '530902', '临翔区', 3, '0883', 100.086486, 23.886562, 3216);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3222, '530926', '耿马傣族佤族自治县', 3, '0883', 99.402495, 23.534579, 3216);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3223, '530925', '双江拉祜族佤族布朗族傣族自治县', 3, '0883', 99.824419, 23.477476, 3216);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3224, '530927', '沧源佤族自治县', 3, '0883', 99.2474, 23.146887, 3216);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3225, '533100', '德宏傣族景颇族自治州', 2, '0692', 98.578363, 24.436694, 3085);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3226, '533123', '盈江县', 3, '0692', 97.93393, 24.709541, 3225);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3227, '533122', '梁河县', 3, '0692', 98.298196, 24.80742, 3225);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3228, '533124', '陇川县', 3, '0692', 97.794441, 24.184065, 3225);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3229, '533103', '芒市', 3, '0692', 98.577608, 24.436699, 3225);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3230, '533102', '瑞丽市', 3, '0692', 97.855883, 24.010734, 3225);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3231, '110000', '北京市', 1, '010', 116.405285, 39.904989, 1);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3232, '110100', '北京城区', 2, '010', 116.405285, 39.904989, 3231);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3233, '110119', '延庆区', 3, '010', 115.985006, 40.465325, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3234, '110116', '怀柔区', 3, '010', 116.637122, 40.324272, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3235, '110109', '门头沟区', 3, '010', 116.105381, 39.937183, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3236, '110113', '顺义区', 3, '010', 116.653525, 40.128936, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3237, '110117', '平谷区', 3, '010', 117.112335, 40.144783, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3238, '110114', '昌平区', 3, '010', 116.235906, 40.218085, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3239, '110101', '东城区', 3, '010', 116.418757, 39.917544, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3240, '110105', '朝阳区', 3, '010', 116.486409, 39.921489, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3241, '110108', '海淀区', 3, '010', 116.310316, 39.956074, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3242, '110107', '石景山区', 3, '010', 116.195445, 39.914601, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3243, '110111', '房山区', 3, '010', 116.139157, 39.735535, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3244, '110106', '丰台区', 3, '010', 116.286968, 39.863642, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3245, '110102', '西城区', 3, '010', 116.366794, 39.915309, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3246, '110115', '大兴区', 3, '010', 116.338033, 39.728908, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3247, '110112', '通州区', 3, '010', 116.658603, 39.902486, 3232);
INSERT INTO region(`id`, `code`, `name`, `level`, `city_code`, `lng`, `lat`, `parent_id`)
VALUES (3248, '110118', '密云区', 3, '010', 116.843352, 40.377362, 3232);
