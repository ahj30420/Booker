package project.booker.repository.PofileRepository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import project.booker.domain.MemberProfile;
import project.booker.domain.QInterest;

import java.util.List;

import static project.booker.domain.QMemberProfile.*;

public class ProfileRepositoryImpl implements ProfileRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ProfileRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 사용자의 관심사와 최소 절반 이상 비슷한 회원 목록 조회 (추천 기능)
     * 1. 무한 스크롤링을 위해 Slice 형식의 페이징 처리
     */
    @Override
    public Slice<MemberProfile> searchSimilarUser(Long profilePk, List<String> interests, Pageable pageable) {

        int pageSize = pageable.getPageSize();

        List<MemberProfile> profiles = queryFactory.selectFrom(memberProfile)
                .join(QInterest.interest1)
                .on(memberProfile.eq(QInterest.interest1.memberProfile))
                .where(
                        QInterest.interest1.interest.in(interests),
                        memberProfile.profilePk.ne(profilePk)
                )
                .groupBy(memberProfile.profilePk)
                .having(memberProfile.profilePk.count().goe(Math.round((double)interests.size() / 2)))
                .offset(pageable.getOffset())
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = false;
        if(profiles.size() > pageSize){
            profiles.remove(pageSize);
            hasNext = true;
        }

        return new SliceImpl<>(profiles, pageable, hasNext);
    }
}
