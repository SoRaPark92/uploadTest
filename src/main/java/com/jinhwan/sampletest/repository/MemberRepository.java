package com.jinhwan.sampletest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jinhwan.sampletest.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
	/**
	 * 로그인 정보 체크
	 * @param userId
	 * @param password
	 * @return
	 */
	List<Member> findByUserIdAndPassword (String userId , String password);
	
	/**
	 * 중복계정이 존재하는지 체크
	 * @param userId
	 * @return
	 */
	List<Member> findByRegNo (String regNo);
	
	/**
	 * 유저 정보 조회
	 * @param userId
	 * @return
	 */
	Member findByUserId (String userId);
	
}
