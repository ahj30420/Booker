package project.booker.repository.InterestRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class InterestRepositoryImpl implements InterestRepositoryCustom{

    private final JdbcTemplate jdbcTemplate;

    /**
     * 관심사 한번에 저장하기 (Batch_Insert 벌크 연산)
     * @param profilePk: 사용자의 프로필 PK
     * @param interestList: 사용자가 입력한 관심사 목록
     */
    @Override
    public void bulkSave(Long profilePk, List<String> interestList) {

        jdbcTemplate.batchUpdate("insert into interest(profile_pk, interest) values(?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, profilePk);
                        ps.setString(2, interestList.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return interestList.size();
                    }
                }
        );

    }

}
