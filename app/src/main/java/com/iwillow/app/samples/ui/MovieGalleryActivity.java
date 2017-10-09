package com.iwillow.app.samples.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iwillow.app.android.ui.view.ClipViewPager;
import com.iwillow.app.android.util.BlurUtil;
import com.iwillow.app.android.util.DimenUtil;
import com.iwillow.app.samples.R;

import java.lang.ref.WeakReference;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class MovieGalleryActivity extends AppCompatActivity {

    ClipViewPager viewPager;
    View containerView;
    private ImageView imageBg;
    private TextView title;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final String[] MOVIE_TITLES = {
            "羞羞的铁拳", "钢铁飞龙之再见奥特曼",
            "看不见的客人", "昆塔：反转星球",
            "情遇曼哈顿", "天才枪手",
            "空天猎", "猩球崛起3：终极之战 ",
            "我的妈呀", "我的爸爸是森林之王",
    };
    private static final int[] MOVIE_COVERS = {
            R.drawable.img_movie_1, R.drawable.img_movie_2,
            R.drawable.img_movie_3, R.drawable.img_movie_4,
            R.drawable.img_movie_5, R.drawable.img_movie_6,
            R.drawable.img_movie_7, R.drawable.img_movie_8,
            R.drawable.img_movie_9, R.drawable.img_movie_10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_gallery);
        viewPager = (ClipViewPager) findViewById(R.id.vp_movie_gallery);
        containerView = findViewById(R.id.rl_movie_container);
        containerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });
        imageBg = (ImageView) findViewById(R.id.iv_movie_bg);
        title = (TextView) findViewById(R.id.tv_movie_title);
        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        layoutParams.width = (int) DimenUtil.dp2px(getResources(), 100);
        viewPager.setPageTransformer(false, new ClipViewPager.ScaleTransformer());
        viewPager.setLayoutParams(layoutParams);
        viewPager.setAdapter(new MoviePicAdapter());
        viewPager.setOffscreenPageLimit(5);
        viewPager.setCurrentItem(0);
        title.setText(MOVIE_TITLES[0]);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                title.setText(MOVIE_TITLES[position]);
                ImageView imageView = (ImageView) viewPager.findViewWithTag(position);
                if (imageView != null) {
                    imageView.setDrawingCacheEnabled(true);
                    Disposable d = Flowable.just(imageView)
                            .filter(new Predicate<ImageView>() {
                                @Override
                                public boolean test(@NonNull ImageView imageView) throws Exception {
                                    return imageView != null;
                                }
                            })
                            .map(new Function<ImageView, Bitmap>() {
                                @Override
                                public Bitmap apply(@NonNull ImageView imageView) throws Exception {
                                    return imageView.getDrawingCache();
                                }
                            })
                            .map(new Function<Bitmap, Bitmap>() {
                                @Override
                                public Bitmap apply(@NonNull Bitmap bitmap) throws Exception {
                                    return BlurUtil.with(new WeakReference<Context>(MovieGalleryActivity.this)).bitmap(bitmap)
                                            .radius(14)
                                            .blur();
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Bitmap>() {
                                @Override
                                public void accept(Bitmap bitmap) throws Exception {
                                    imageBg.setImageBitmap(bitmap);
                                }
                            });
                    compositeDisposable.add(d);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Disposable d = Flowable.just(MOVIE_COVERS[0])
                .map(new Function<Integer, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull Integer integer) throws Exception {
                        return BitmapFactory.decodeResource(getResources(), integer);
                    }
                })
                .filter(new Predicate<Bitmap>() {
                    @Override
                    public boolean test(@NonNull Bitmap bitmap) throws Exception {
                        return bitmap != null;
                    }
                })
                .map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull Bitmap bitmap) throws Exception {
                        return BlurUtil.with(new WeakReference<Context>(MovieGalleryActivity.this)).bitmap(bitmap)
                                .radius(14)
                                .blur();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        imageBg.setImageBitmap(bitmap);
                    }
                });

        compositeDisposable.add(d);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    public static class MoviePicAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return MOVIE_COVERS.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setTag(position);
            imageView.setImageResource(MOVIE_COVERS[position]);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public final void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);

        }

        @Override
        public final boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
