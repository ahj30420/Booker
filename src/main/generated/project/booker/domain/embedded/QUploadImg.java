package project.booker.domain.embedded;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUploadImg is a Querydsl query type for UploadImg
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUploadImg extends BeanPath<UploadImg> {

    private static final long serialVersionUID = -1739910523L;

    public static final QUploadImg uploadImg = new QUploadImg("uploadImg");

    public final StringPath RealImgName = createString("RealImgName");

    public final StringPath StoreImgName = createString("StoreImgName");

    public QUploadImg(String variable) {
        super(UploadImg.class, forVariable(variable));
    }

    public QUploadImg(Path<? extends UploadImg> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUploadImg(PathMetadata metadata) {
        super(UploadImg.class, metadata);
    }

}

