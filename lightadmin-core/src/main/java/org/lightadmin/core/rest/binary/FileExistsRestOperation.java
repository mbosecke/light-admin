package org.lightadmin.core.rest.binary;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.lightadmin.api.config.annotation.FileReference;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.data.rest.repository.AttributeMetadata;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.getFile;

public class FileExistsRestOperation extends AbstractFileRestOperation {

    protected FileExistsRestOperation(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration, Object entity) {
        super(configuration, lightAdminConfiguration, entity);
    }

    public boolean perform(AttributeMetadata attrMeta) throws IOException {
        if (attrMeta.type().equals(byte[].class)) {
            return ArrayUtils.isNotEmpty((byte[]) attrMeta.get(entity));
        }

        if (attrMeta.hasAnnotation(FileReference.class)) {
            FileReference fileReference = attrMeta.annotation(FileReference.class);

            if (getFile(fileReference.baseDirectory()).exists()) {
                File file = referencedFile(attrMeta);
                return file.exists() && FileUtils.sizeOf(file) > 0;
            } else {
                File file = fileStorageFile(attrMeta);
                return file.exists() && FileUtils.sizeOf(file) > 0;
            }
        }
        return false;
    }
}