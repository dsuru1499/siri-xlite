package siri_lite.service.lines_discovery;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import siri_xlite.common.Configuration;
import siri_xlite.service.common.ParametersFactory;
import siri_xlite.service.lines_discovery.LinesDiscoveryParameters;

@Data
@EqualsAndHashCode(callSuper = true)
public class LinesDiscoveryParametersFactory extends ParametersFactory<LinesDiscoveryParameters> {

    static {
        ParametersFactory.register(LinesDiscoveryParameters.class, new LinesDiscoveryParametersFactory());
    }

    @Override
    protected LinesDiscoveryParameters create(RoutingContext context) throws Exception {
        LinesDiscoveryParameters parameters = new LinesDiscoveryParameters();
        // Configuration properties = getConfigutation(context);
        Configuration properties = new Configuration();
        parameters.configure(properties);
        parameters.validate();

        return parameters;
    }

    // private String getServiceName(AsyncContext context) {
    //
    // HttpServletRequest request = (HttpServletRequest) context.getRequest();
    // String path = request.getPathInfo();
    // int first = path.indexOf('/') + 1;
    // int last = (path.lastIndexOf('.') != -1) ? path.lastIndexOf('.') : path.length();
    // String value = path.substring(first, last);
    // String result = Stream.of(value).map(t -> t.split("-")).flatMap(Arrays::stream).map(WordUtils::capitalize)
    // .collect(Collectors.joining());
    // return result;
    // }
    //
    // private Configuration getConfigutation(AsyncContext context) {
    // Configuration result = new Configuration();
    //
    // HttpServletRequest request = (HttpServletRequest) context.getRequest();
    // result.put(DefaultParameters.PRODUCER_DOMAIN, "default");
    // result.put(DefaultParameters.PRODUCER_NAME, "default");
    //
    // for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
    // result.putAll(entry.getKey(), Arrays.asList(entry.getValue()));
    // }
    // for (String key : Collections.list(request.getHeaderNames())) {
    // result.putAll(key, Collections.list(request.getHeaders(key)));
    // }
    //
    // return result;
    // }
}
