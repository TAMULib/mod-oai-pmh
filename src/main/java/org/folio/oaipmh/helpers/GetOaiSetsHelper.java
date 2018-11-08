package org.folio.oaipmh.helpers;

import io.vertx.core.Context;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import me.escoffier.vertx.completablefuture.VertxCompletableFuture;
import org.folio.oaipmh.Request;
import org.folio.oaipmh.ResponseHelper;
import org.folio.rest.jaxrs.resource.Oai.GetOaiSetsResponse;
import org.openarchives.oai._2.ListSetsType;
import org.openarchives.oai._2.OAIPMH;

import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;

/**
 * Helper class that contains business logic for retrieving OAI-PMH set structure of a repository.
 */
public class GetOaiSetsHelper extends AbstractHelper {
  private static final Logger logger = LoggerFactory.getLogger(GetOaiSetsHelper.class);

  @Override
  public CompletableFuture<Response> handle(Request request, Context ctx) {
    CompletableFuture<Response> future = new VertxCompletableFuture<>(ctx);
    try {
      OAIPMH oai = buildBaseResponse(request)
        .withListSets(new ListSetsType()
          .withSets(getSupportedSetTypes()));

      String response = ResponseHelper.getInstance().writeToString(oai);
      future.complete(GetOaiSetsResponse.respond200WithApplicationXml(response));
    } catch (Exception e) {
      logger.error("Error happened while processing ListSets verb request", e);
      future.completeExceptionally(e);
    }

    return future;
  }
}
