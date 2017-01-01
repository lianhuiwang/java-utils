package collection;

import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * @auther: lianhui
 * @date: 16/11/29
 */
public class AppCache {

	public static void main(String[] args) throws ExecutionException {
		LoadingCache cache = CacheBuilder.newBuilder()
				.maximumSize(2)
				.removalListener(new RemovalListener<Integer, Long>() {

			@Override
			public void onRemoval(RemovalNotification<Integer, Long> rm) {
					System.out.println("remove=" + rm.getKey());
			}
		}).build(new CacheLoader<Integer, Long>() {

			public Long load(Integer key) {
				return Long.valueOf(key);
			}
		});
		for (int i = 0; i < 10; i++) {
			cache.put(i, i);
		}

		for (int i = 0; i < 10; i++) {
			cache.get(i);
		}

	}
}
