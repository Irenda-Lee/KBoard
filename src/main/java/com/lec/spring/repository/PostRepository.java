package com.lec.spring.repository;

// Repository layer(aka. Data layer)
// DataSource (DB) 등에 대한 직접적인 접근

import com.lec.spring.domain.Post;

import java.util.List;

public interface PostRepository {
    // 새 글 작성하는 동작 (INSERT) <- Post (작성자, 제목, 내용) 를 받아서 만듦
    int save(Post post); // DML(MyBatis) 에서는 정수로 받아옴

    // 특정 id 의 글 내용 읽기 (SELECT) => Post 에 담기
    // 만약, 해당 ID 의 글이 없으면 NULL 리턴
    Post findById(Long id);

    // 특정 id 글 조회수 +1 증가 (UPDATE)
    int incViewCnt(Long id);

    // 전체 글 목록 : 최신순 (SELECT * ORDER BY id DESC) => List<>
    List<Post> findAll();

    // 특정 id 글 수정 (제목, 내용) (UPDATE)
    int update(Post post);

    // 특정 id 글 삭제하기 (DELETE)
    int delete(Post post);

    // 페이징
    // from 부터 rows 개 만큼 SELECT
    List<Post> selectFromRow(int from, int rows);

    // 전체 글의 개수
    int countAll();



}
