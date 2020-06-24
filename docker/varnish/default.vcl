#
# This is an example VCL file for Varnish.
#
# It does not do anything by cluster, delegating control to the
# builtin VCL. The builtin VCL is called when there is no explicit
# return statement.
#
# See the VCL chapters in the Users Guide at https://www.varnish-cache.org/docs/
# and https://www.varnish-cache.org/trac/wiki/VCLExamples for more examples.

# Marker to tell the VCL compiler that this VCL has been adapted to the
# new 4.0 format.
vcl 4.0;
import directors;    # load the directors

acl purge {
        "localhost";
        "127.0.0.1"/24;
        "172.20.0.0"/16;
}

# Default backend definition. Set this to point to your content server.

backend server_1 {
    .host = "siri-xlite";
    .port = "8443";
    .probe = {
        .url = "/siri-xlite";
        .interval = 10s;
        .timeout = 1s;
        .window = 5;
        .threshold = 3;
    }
}

sub vcl_init {
    new cluster = directors.round_robin();
    cluster.add_backend(server_1);
}

sub vcl_recv {
    # Happens before we check if we have this in cache already.
    #
    # Typically you clean up the request here, removing cookies you don't need,
    # rewriting the request, etc.
    

    # allow PURGE from localhost
    if (req.method == "PURGE") {
        if (!client.ip ~ purge) {
            return(synth(405,"Not allowed."));
        }
        return (purge);
        #ban("req.url ~ " + req.url);
    }
    
    # send all traffic to the director:
    set req.backend_hint = cluster.backend();
}

sub vcl_backend_response {
    # Happens after we have read the response headers from the backend.
    #
    # Here you clean the response headers, removing silly Set-Cookie headers
    # and other mistakes your backend does.
}

sub vcl_deliver {
    # Happens when we have all the pieces we need, and are about to send the
    # response to the client.
    #
    # You can do accounting or modifying the final object here.
}
