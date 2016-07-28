import rx.Observable;
import rx.Subscriber;

/**
 * Created by Mohamed Kamal on 7/24/2016.
 */
public class HelloWorld {
    public static void main(String[] args) {




        generateNumberLine(10)
                .subscribe(e -> System.out.print(e + " "));
    }

    private static Observable<Integer> generateNumberLine(int n) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
                                     @Override
                                     public void call(Subscriber<? super Integer> sub) {
                                         for (int i = 0; i < n; i++) {
                                             sub.onNext(i + 1);
                                         }
                                         sub.onCompleted();
                                     }
                                 }
        );
    }
}
