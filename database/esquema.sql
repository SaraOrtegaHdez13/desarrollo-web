-- MySQL dump 10.13  Distrib 8.4.13, for Linux (x86_64)
--
-- Host: localhost    Database: luzioteamtime
-- ------------------------------------------------------
-- Server version	8.4.13-0ubuntu0.26.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `asignaciones_tarea`
--

DROP TABLE IF EXISTS `asignaciones_tarea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignaciones_tarea` (
  `id` int NOT NULL,
  `estado_al_recibir` varchar(255) DEFAULT NULL,
  `estado_al_salir` varchar(255) DEFAULT NULL,
  `fecha_fin` datetime(6) DEFAULT NULL,
  `fecha_inicio` datetime(6) DEFAULT NULL,
  `id_tarea` int NOT NULL,
  `id_usuario` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhbd5337cm1sjt6e70qn8o9evq` (`id_tarea`),
  KEY `FK50a9ugjthmtadwdfmyj5rr3l2` (`id_usuario`),
  CONSTRAINT `FK50a9ugjthmtadwdfmyj5rr3l2` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FKhbd5337cm1sjt6e70qn8o9evq` FOREIGN KEY (`id_tarea`) REFERENCES `tareas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asignaciones_tarea_seq`
--

DROP TABLE IF EXISTS `asignaciones_tarea_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignaciones_tarea_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `equipos`
--

DROP TABLE IF EXISTS `equipos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipos` (
  `id` int NOT NULL,
  `archivado` bit(1) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `equipos_seq`
--

DROP TABLE IF EXISTS `equipos_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipos_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `miembros_equipo`
--

DROP TABLE IF EXISTS `miembros_equipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `miembros_equipo` (
  `id` int NOT NULL,
  `rol` varchar(20) NOT NULL,
  `id_equipo` int NOT NULL,
  `id_usuario` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK38rbhpvsouo9x4w1219epy28l` (`id_usuario`,`id_equipo`),
  KEY `FK5ik7lcw142hlkycv4ih34bc7t` (`id_equipo`),
  CONSTRAINT `FK5ik7lcw142hlkycv4ih34bc7t` FOREIGN KEY (`id_equipo`) REFERENCES `equipos` (`id`),
  CONSTRAINT `FKjte6kk3uirm1hg4pye0gph0pg` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `miembros_equipo_seq`
--

DROP TABLE IF EXISTS `miembros_equipo_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `miembros_equipo_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `revisiones_tarea`
--

DROP TABLE IF EXISTS `revisiones_tarea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `revisiones_tarea` (
  `id` int NOT NULL,
  `observacion` varchar(500) DEFAULT NULL,
  `id_tarea` int NOT NULL,
  `fecha_entrega` datetime(6) NOT NULL,
  `fecha_revision` datetime(6) DEFAULT NULL,
  `numero_entrega` int NOT NULL,
  `resultado` varchar(20) NOT NULL,
  `id_entregado_por` int NOT NULL,
  `id_revisor` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg2tls3l9qfysmn85kf19a6yb` (`id_tarea`),
  KEY `FK6m60rpky3kajgw9so4pkpwdn7` (`id_entregado_por`),
  KEY `FKpdu43ns32tchsahjyj2f875yp` (`id_revisor`),
  CONSTRAINT `FK6m60rpky3kajgw9so4pkpwdn7` FOREIGN KEY (`id_entregado_por`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FKg2tls3l9qfysmn85kf19a6yb` FOREIGN KEY (`id_tarea`) REFERENCES `tareas` (`id`),
  CONSTRAINT `FKpdu43ns32tchsahjyj2f875yp` FOREIGN KEY (`id_revisor`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `revisiones_tarea_seq`
--

DROP TABLE IF EXISTS `revisiones_tarea_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `revisiones_tarea_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sesiones`
--

DROP TABLE IF EXISTS `sesiones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sesiones` (
  `id` int NOT NULL,
  `duracion_minutos` int DEFAULT NULL,
  `fin` datetime(6) DEFAULT NULL,
  `inicio` datetime(6) DEFAULT NULL,
  `id_tarea` int NOT NULL,
  `id_usuario` int NOT NULL,
  `duracion_segundos` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfbaoq71xmovp7mmcxdyhamuiu` (`id_tarea`),
  KEY `FK8cv9h8j6my5ft6atqfu9uurd` (`id_usuario`),
  CONSTRAINT `FK8cv9h8j6my5ft6atqfu9uurd` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FKfbaoq71xmovp7mmcxdyhamuiu` FOREIGN KEY (`id_tarea`) REFERENCES `tareas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sesiones_seq`
--

DROP TABLE IF EXISTS `sesiones_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sesiones_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tableros`
--

DROP TABLE IF EXISTS `tableros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tableros` (
  `id` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `id_equipo` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2tevicnxim1mjc3k2w0u7fva3` (`id_equipo`),
  CONSTRAINT `FK2tevicnxim1mjc3k2w0u7fva3` FOREIGN KEY (`id_equipo`) REFERENCES `equipos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tableros_seq`
--

DROP TABLE IF EXISTS `tableros_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tableros_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tareas`
--

DROP TABLE IF EXISTS `tareas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tareas` (
  `id` int NOT NULL,
  `descripcion` varchar(1000) DEFAULT NULL,
  `estado` varchar(20) NOT NULL,
  `fecha_completada` datetime(6) DEFAULT NULL,
  `fecha_limite` date DEFAULT NULL,
  `observacion` varchar(500) DEFAULT NULL,
  `titulo` varchar(150) NOT NULL,
  `id_responsable` int NOT NULL,
  `id_tablero` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK10y5551cfjibd7uib2j3e7ln2` (`id_responsable`),
  KEY `FK8iusmp2p4epmarelkgequty38` (`id_tablero`),
  CONSTRAINT `FK10y5551cfjibd7uib2j3e7ln2` FOREIGN KEY (`id_responsable`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FK8iusmp2p4epmarelkgequty38` FOREIGN KEY (`id_tablero`) REFERENCES `tableros` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tareas_seq`
--

DROP TABLE IF EXISTS `tareas_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tareas_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario_seq`
--

DROP TABLE IF EXISTS `usuario_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL,
  `correo` varchar(255) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK2mlfr087gb1ce55f2j87o74t` (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuarios_seq`
--

DROP TABLE IF EXISTS `usuarios_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-22 22:58:23
