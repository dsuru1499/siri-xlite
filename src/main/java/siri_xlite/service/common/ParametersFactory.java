package siri_xlite.service.common;

import com.google.common.base.CaseFormat;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import siri_xlite.common.Parameters;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class ParametersFactory<T> {

    private static Map<Class<? extends Parameters>, ParametersFactory<?>> _factories = new HashMap<>();

    protected abstract Parameters create(RoutingContext context) throws Exception;

    public static <T> void register(Class<? extends Parameters> clazz, ParametersFactory<T> factory) {
        _factories.put(clazz, factory);
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<? extends Parameters> clazz, RoutingContext context) throws Exception {
        ParametersFactory<?> factory = _factories.get(clazz);
        if (factory == null) {
            try {
                Class.forName(getClassName(clazz));
                factory = _factories.get(clazz);
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

        Parameters parameters = factory.create(context);
        parameters.validate();

        return (T) parameters;
    }

    private static String getClassName(Class<? extends Parameters> clazz) {
        String name = clazz.getSimpleName();
        String service = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                name.substring(0, name.lastIndexOf("Parameters")));
        return "siri_xlite.service." + service + "." + name + "Factory";
    }

}
