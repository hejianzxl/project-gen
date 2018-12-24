package com.yiyi.tang.projectgen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

/**
 * @author tangmingjian 2018-12-01 下午3:14
 **/
@Component
public class FileServiceListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private FileServiceFactory fileServiceFactory;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        if (applicationContext.getParent() == null) {
            Map<String, AbstractFileService> beans = applicationContext.getBeansOfType(AbstractFileService.class);
            Iterator<String> it = beans.keySet().iterator();
            while (it.hasNext()) {
                fileServiceFactory.registerService(beans.get(it.next()));
            }
        }
//        SpringFactoriesLoader.loadFactories(AbstractFileService.class, null).stream()
//                .forEach(fileServiceFactory::registerService);
    }
}
