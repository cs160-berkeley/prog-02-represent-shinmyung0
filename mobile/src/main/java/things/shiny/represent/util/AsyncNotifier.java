package things.shiny.represent.util;

/**
 * AsyncNotifiers notifyListeners() on the AsyncListener they have setListener() as
 * Created by shinmyung0 on 3/10/16.
 */
public interface AsyncNotifier {
    public void setListener(AsyncListener listener);
}
