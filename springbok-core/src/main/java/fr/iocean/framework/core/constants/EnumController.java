package fr.iocean.framework.core.constants;

import fr.iocean.framework.core.i18n.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public/constants")
@Slf4j
public class EnumController {

    @Autowired
    MessageService messageService;
    

    @RequestMapping
    public Map<String, List<EnumWithLang>> getValuesForEnum() {
        return loadRestConstants();
    }

    public Map<String, List<EnumWithLang>> loadRestConstants() {
        Map<String, List<EnumWithLang>> enums = new HashMap<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(RestEnum.class));
        try {
            for (BeanDefinition bd : scanner.findCandidateComponents("fr")) {
                Class<?> cls = Class.forName(bd.getBeanClassName());
                if (cls.isEnum()) {
                    List<EnumWithLang> values = new ArrayList<>();
                    for (Object enumConstant : cls.getEnumConstants()) {
                        values.add(new EnumWithLang(
                                messageService.getMessage(((RestEnum) enumConstant).getI18nKey()), enumConstant));
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
        public final String label;
        public final Object value;

        public EnumWithLang(String label, Object value) {
            this.label = label;
            this.value = value;
        }
    }
}
