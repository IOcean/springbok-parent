package fr.iocean.framework.core.constants;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public/constants")
@Slf4j
public class EnumController {

    private final Map<String, List<EnumWithLang>> enums = new HashMap<>();

    @PostConstruct
    public void preLoad() {
        loadRestConstants();
    }

    @RequestMapping
    public Map<String, List<EnumWithLang>> getValuesForEnum() {
        return enums;
    }

    public Map<String, List<EnumWithLang>> loadRestConstants() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(RestEnum.class));
        try {
            for (BeanDefinition bd : scanner.findCandidateComponents("fr")) {
                Class<?> cls = Class.forName(bd.getBeanClassName());
                if (cls.isEnum()) {
                    List<EnumWithLang> values = new ArrayList<>();
                    for (Object enumConstant : cls.getEnumConstants()) {
                        values.add(new EnumWithLang(((RestEnum) enumConstant).getI18nKey(), enumConstant));
                    }
                    enums.put(cls.getSimpleName(), values);
                }

            }
        } catch (ClassNotFoundException e) {
            log.info("No Rest enum found");
        }
        log.info("Exposed Rest enum : " + Arrays.toString(enums.keySet().toArray()));
        return enums;
    }

    static class EnumWithLang {
        public final String lang;
        public final Object value;

        public EnumWithLang(String lang, Object value) {
            this.lang = lang;
            this.value = value;
        }
    }
}
