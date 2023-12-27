package project.booker.domain.embedded;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInterest is a Querydsl query type for Interest
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QInterest extends BeanPath<Interest> {

    private static final long serialVersionUID = -1880998329L;

    public static final QInterest interest = new QInterest("interest");

    public final StringPath interest1 = createString("interest1");

    public final StringPath interest2 = createString("interest2");

    public final StringPath interest3 = createString("interest3");

    public final StringPath interest4 = createString("interest4");

    public final StringPath interest5 = createString("interest5");

    public QInterest(String variable) {
        super(Interest.class, forVariable(variable));
    }

    public QInterest(Path<? extends Interest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInterest(PathMetadata metadata) {
        super(Interest.class, metadata);
    }

}

