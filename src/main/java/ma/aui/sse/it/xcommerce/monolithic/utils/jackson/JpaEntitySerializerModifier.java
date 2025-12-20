package ma.aui.sse.it.xcommerce.monolithic.utils.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.springframework.util.ClassUtils;

import jakarta.persistence.Entity;

public class JpaEntitySerializerModifier extends BeanSerializerModifier {

    @Override
    public JsonSerializer<?> modifySerializer(
            SerializationConfig config,
            BeanDescription beanDesc,
            JsonSerializer<?> serializer) {

        Class<?> beanClass = beanDesc.getBeanClass();

        // On récupère la "vraie" classe derrière un proxy Hibernate éventuel
        Class<?> userClass = ClassUtils.getUserClass(beanClass);

        // Critère : classe annotée @Entity
        if (userClass.isAnnotationPresent(Entity.class)) {
            return new ForbiddenEntitySerializer();
        }

        return serializer;
    }

}