package project.booker.repository.PofileRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import project.booker.domain.MemberProfile;

import java.util.List;

public interface ProfileRepositoryCustom {

    Slice<MemberProfile> searchSimilarUser(Long profilePk, List<String> interests, Pageable pageable);

}
