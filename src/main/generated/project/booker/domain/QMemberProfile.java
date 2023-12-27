package project.booker.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberProfile is a Querydsl query type for MemberProfile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberProfile extends EntityPathBase<MemberProfile> {

    private static final long serialVersionUID = -471426520L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberProfile memberProfile = new QMemberProfile("memberProfile");

    public final project.booker.domain.embedded.QUploadImg img;

    public final project.booker.domain.embedded.QInterest interest;

    public final StringPath intro = createString("intro");

    public final QMember member;

    public final StringPath nickname = createString("nickname");

    public final NumberPath<Long> ProfileIdx = createNumber("ProfileIdx", Long.class);

    public QMemberProfile(String variable) {
        this(MemberProfile.class, forVariable(variable), INITS);
    }

    public QMemberProfile(Path<? extends MemberProfile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberProfile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberProfile(PathMetadata metadata, PathInits inits) {
        this(MemberProfile.class, metadata, inits);
    }

    public QMemberProfile(Class<? extends MemberProfile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.img = inits.isInitialized("img") ? new project.booker.domain.embedded.QUploadImg(forProperty("img")) : null;
        this.interest = inits.isInitialized("interest") ? new project.booker.domain.embedded.QInterest(forProperty("interest")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

