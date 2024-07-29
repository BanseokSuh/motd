# MOTD

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https://github.com/BanseokSuh/motd&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

<br>

## 프로젝트 소개

MOTD는 <strong>Miracle On ThursDay</strong>의 약자로, 매주 목요일에 선한 일을 하여 세상에 선한 영향력을 퍼뜨리는데에 동참하는 사람들을 위한 커뮤니티 서비스입니다.

(2024.07.28 기준) <br>
아직 프로젝트에 대한 기획이 준비 중이기에, 아래의 기본적인 기능들만 구현되어 있습니다.<br>
지속적으로 기능 추가, 리팩토링 및 고도화가 진행될 예정입니다.<br>
- 회원가입
- 로그인
- 게시글 조회/작성/수정/삭제
- 댓글 작성
- 좋아요
- 실시간 알림 (댓글 작성, 좋아요 시 작성자에게 실시간 메시지 발송)

<br>

## 기술 스택

<div align=center> 
  <img src="https://img.shields.io/badge/java 17-007396?style=for-the-badge&logo=java&logoColor=white" alt=""> 
  <br>
  <img src="https://img.shields.io/badge/springboot 3.3-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt=""> 
  <br>
  <img src="https://img.shields.io/badge/APACHE KAFKA-231F20?style=for-the-badge&logo=apachekafka&logoColor=white" alt="">
  <br>
  <img src="https://img.shields.io/badge/postgresql-4479A1?style=for-the-badge&logo=postgresql&logoColor=white" alt=""> 
  <img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white" alt=""> 
  <br>
  <img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white" alt=""> 
  <br>
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white" alt="">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white" alt="">
  <br>
</div>

<br>

## API 문서

- [MOTD API Document](https://documenter.getpostman.com/view/10226658/2sA3kaBdxn)

<br>

## 주요 기능

- 로그인 / 회원가입 - Spring Security를 이용한 JWT 토큰 기반 인증
- Redis를 이용한 유저 정보 caching
- 게시글 조회/작성/수정/삭제
- 댓글 작성
- 좋아요 기능
- Kafka/SSE를 이용한 실시간 알림

<br>

## 프로젝트 구조

- [프로젝트 구조](https://github.com/BanseokSuh/motd/wiki/Project-structure)

<br>

## 트러블슈팅

- [트러블슈팅](https://github.com/BanseokSuh/motd/wiki/%5BTroubleShooting%5D-troubleshooting-1)

<br>

## 시작 가이드

준비중입니다.

<br>

## 

