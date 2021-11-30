package com.astudio.progressmonitor.scheme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.astudio.progressmonitor.R;
import com.astudio.progressmonitor.group.NavigationActivity;
import com.astudio.progressmonitor.modules.App;
import com.astudio.progressmonitor.post.RepositoryPost;
import com.astudio.progressmonitor.user.UserEasy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StructuralSchemeActivity extends NavigationActivity {


    private static final String TAG = "StructSchemeActivity";
    List<SchemeElement> schemeElements = new ArrayList<>();
    private List<ElementCoordinates> coordinateList = new ArrayList<>();
    LinearLayout canvasLayout;
    //private int levelTree = 0; // Only for test
    //private Map <Integer, Integer> branchBoundaries = new HashMap<>();
    private int canvasWidth = 2000;
    private int canvasHeight = 2000;
    private int canvasPaddingHorizontal = 100; // отступ элементов от края канваса по горизонтали
    private int canvasPaddingVertical = 200; // отступ элементов от края канваса по вертикали (а также расстояние между элементами)
    private int elementWidth = 600;
    private int elementHeight = 100;
    private int elementTextSize = 48;
    private boolean moveRight = false; // режим смещения
    private int rightExtremePosition = 0; // крайняя правая координата по горизонтали последнего (в цикле) элемента
    private int bottomExtremePosition = 0; // крайняя нижняя координата по вертикали последнего элемента
    private String titleScheme;

    //private float scalingFactor = 1f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bundle bundle = getIntent().getBundleExtra(GLOBAL_DATA);
        //globalData = Objects.requireNonNull(bundle).getParcelable(GLOBAL_DATA);
        Intent intent = getIntent();
        globalData = intent.getParcelableExtra(GLOBAL_DATA);

        setContentView(R.layout.activity_structural_scheme);
        canvasLayout = findViewById(R.id.canvas_layout);
        //FloatingActionButton fb = findViewById(R.id.fab4);
        //if (false) fb.setVisibility(View.GONE);

//        Button btnHigh = findViewById(R.id.btn_scale_high);
//        btnHigh.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                scalingFactor += 0.2f;
//                drawScheme();
//            }
//        });
//
//        Button btnLow = findViewById(R.id.btn_scale_low);
//        btnLow.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                scalingFactor -= 0.2f;
//                drawScheme();
//            }
//        });

        RepositoryPost repositoryPost = App.getInstance().getRepositoryPost();

        LiveData<List<SchemeElement>> ldUsers = repositoryPost.getSchemeElements(globalData.groupToken);

        ldUsers.observe(this, new Observer<List<SchemeElement>>() {
            @Override
            public void onChanged(List <SchemeElement> value) {
                //Log.e("LiveData-Observer", "idExample-" + this);
                schemeElements.clear();
                if (value != null) {
                    //Log.e("LiveData-Observer", "Elements: " + schemeElements);
                    schemeElements.addAll(value);
                    coordinateList.clear();
                    checkScreenSize();
                    recursiveTreeConstruction(schemeElements, 0);
                    drawScheme();
                }
            }
        });

        titleScheme = getString(R.string.structure_scheme);
        if (globalData.groupName != null) {
            titleScheme = titleScheme.concat(globalData.groupName);
        }

        //recursiveTreeConstruction(setElementList(), 0);
//        defineBoundariesAllBranch(); // only debug
//        MySysUtil.PrintMap<Integer, Integer> printMap = new MySysUtil.PrintMap<>();
//        printMap.executePrint(branchBoundaries, "Keeper");

        Log.e(TAG, "onCreate");
    }


    private void drawScheme(){
        canvasLayout.removeAllViews();
        Rectangle rectangle = new Rectangle(this);
        rectangle.setData(coordinateList);
        //rectangle.setScaleX(scalingFactor);
        //rectangle.setScaleY(scalingFactor);

        calculationCanvasSize();
        canvasLayout.addView(rectangle, canvasWidth, canvasHeight);
    }


    private void checkScreenSize(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        Log.e(TAG, "height: " + height);
        Log.e(TAG, "width: " + width);

        if (width < 600 || height < 600) {
            elementHeight = elementHeight / 4;
            elementWidth = elementWidth / 4;
            elementTextSize = elementTextSize / 4;
//            canvasWidth = Math.round(canvasWidth * 0.2f);
        } else if (width < 800 || height < 800) {
            elementHeight = elementHeight / 2;
            elementWidth = elementWidth / 2;
            elementTextSize = elementTextSize / 2;
        }
    }


    private void calculationCanvasSize(){
        canvasWidth = rightExtremePosition + canvasPaddingHorizontal;
        canvasHeight = bottomExtremePosition + canvasPaddingVertical + 1200; //TODO: only test!
    }


    private void recursiveTreeConstruction(List<SchemeElement> schemeElements, int parentId){
        for(SchemeElement element: schemeElements){
            //Log.e("Rec", "Ищем потомков parentId:" + parentId);
            //Log.e("Rec", "Текущий элемент:" + element);
            if (element.parentId == parentId) {
                //levelTree++; // Only for test
                //Log.e("Rec", "Потомок найден, текущий уровень: " + levelTree );
                calculationChildCoordinates(element);
                moveRight = false; // потомок найден, значит движение по ветке вниз, смещение не нужно
                recursiveTreeConstruction(schemeElements, element.id);
            }
            //Log.e("Rec", "Потомок не найден, текущий уровень: " + levelTree );
        }
        // здесь мы окажемся когда достигли низа текущей ветки (то есть у текущего элемента нет потомков)
        moveRight = true; // движение по ветке вверх и вправо, активируем режим смещения
        //levelTree--; // Only for test
        //Log.e("Rec", "Конец ветки, поднимаемся на уровень: " + levelTree );
    }


    // Define child element coordinates, and add element to list
    private void calculationChildCoordinates(SchemeElement element) {
        int x1 = canvasPaddingHorizontal, y1 = 0, x2 = canvasPaddingHorizontal + elementWidth, y2 = elementHeight;
        String lineMode = "root"; // first element without line
        int parentRightBound = 0; // for define line length

        for (ElementCoordinates data: coordinateList){

            // get parent coordinates  // ищем в существующем списке координат координаты родителя текущего элемента
            if (element.parentId == data.id){
                if (moveRight){
                    x1 += rightExtremePosition;
                    x2 += rightExtremePosition;
                    lineMode = "combine";
                } else {
                    x1 = data.x1;
                    x2 = data.x2;
                    lineMode = "simple";
                }
                y1 = data.y1;
                y2 = data.y2;
                parentRightBound = data.x2;
            }

        }

        ElementCoordinates elementCoordinates = new ElementCoordinates(element.id, element.parentId, element.name, element.post);
        elementCoordinates.setCoordinates(x1, y1 + canvasPaddingVertical, x2, y2 + canvasPaddingVertical, lineMode, parentRightBound);
        //Log.e("E", "C:" + elementCoordinates);
        coordinateList.add(elementCoordinates);
        defineBottomExtremePosition(y2 + canvasPaddingVertical);
        rightExtremePosition = x2;
    }


    private void defineBottomExtremePosition(int position){
        if (bottomExtremePosition < position) bottomExtremePosition = position;
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(GLOBAL_DATA, globalData);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        globalData = savedInstanceState.getParcelable(GLOBAL_DATA);
    }


    private Rect rect;

    private class Rectangle extends View {

        private Paint paintRect = new Paint();
        private Paint paintText;
        private Paint paintTitle;
        private List<ElementCoordinates> coordinateList;
        //private float widthString;
        private float textSize;

//        private ScaleGestureDetector mScaleDetector;
//        private float mScaleFactor = 1.f;

// centring text https://stackoverflow.com/questions/15609426/draw-text-inside-a-filled-rectangle-using-canvas-android

        public Rectangle(Context context) {
            super(context);
            rect = new Rect();
            paintRect.setColor(Color.RED);
            paintRect.setStrokeWidth(6);
            paintRect.setStyle(Paint.Style.STROKE);
            paintText= new Paint();
            paintText.setStyle(Paint.Style.FILL_AND_STROKE);
            paintText.setColor(Color.BLACK);
            paintText.setTextSize(elementTextSize);  //set text size
            //widthString = paintText.measureText("textExample")/2;
            textSize = paintText.getTextSize();

            paintTitle= new Paint();
            paintTitle.setStyle(Paint.Style.STROKE);
            paintTitle.setColor(Color.BLACK);
            float titleTextSize = elementTextSize * 1.2f;
            paintTitle.setTextSize(titleTextSize);

//            mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        }


//        @Override
//        public boolean onTouchEvent(MotionEvent ev) {
//            // Let the ScaleGestureDetector inspect all events.
//            mScaleDetector.onTouchEvent(ev);
//            return true;
//        }


//        private class ScaleListener
//                extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//            @Override
//            public boolean onScale(ScaleGestureDetector detector) {
//                mScaleFactor *= detector.getScaleFactor();
//
//                // Don't let the object get too small or too large.
//                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
//
//                invalidate();
//                return true;
//            }
//        }


        public Rectangle(Context context, AttributeSet attrs) {
            super(context, attrs);
        }


        @Override
        public void onDraw(Canvas canvas) {

//            super.onDraw(canvas);
//            canvas.save();
//            canvas.scale(mScaleFactor, mScaleFactor);

            getParent().requestDisallowInterceptTouchEvent(true);

            canvas.drawARGB(80, 102, 204, 255);

            canvas.drawText(titleScheme, 100, 100, paintTitle);

            for ( int i=0; i<coordinateList.size(); i++ ){

                int x1 = coordinateList.get(i).x1;
                int y1 = coordinateList.get(i).y1;
                int x2 = coordinateList.get(i).x2;
                int y2 = coordinateList.get(i).y2;
                int parentRightBound = coordinateList.get(i).parentRightBound;

                rect.set(x1, y1, x2, y2);
                canvas.drawRect(rect, paintRect);
                paintText.setTextAlign(Paint.Align.CENTER);
                //canvas.drawText(coordinateList.get(i).post, rect.centerX(), rect.centerY() + textSize/2, paintText); // elementHeight = 60
                canvas.drawText(coordinateList.get(i).post, rect.centerX(), rect.centerY() - 6, paintText);
                canvas.drawText(coordinateList.get(i).name, rect.centerX(), rect.centerY() + textSize - 6, paintText);
                //rect.offset(data.get(i).offsetX, data.get(i).offsetY);

                int lineX1 = (x2 - x1)/2 + x1;

                switch (coordinateList.get(i).lineMode){
                    case "simple":
                        canvas.drawLine(lineX1, y1, lineX1, y1 - canvasPaddingVertical + elementHeight, paintRect);
                        break;
                    case "combine":
                        int verticalSize = y1 - (canvasPaddingVertical - elementHeight)/2;
                        canvas.drawLine(parentRightBound - (elementWidth/2), verticalSize, lineX1, verticalSize, paintRect); // horizontal part
                        canvas.drawLine(lineX1, y1, lineX1, verticalSize, paintRect); // vertical part
                        break;
                    case "root":
                        break;
                }

            }

//            canvas.restore();
        }


        public void setData(List<ElementCoordinates> dataList){
            //if (coordinateList != null) this.coordinateList.clear();
            this.coordinateList = dataList;
            //Log.e(TAG, "coordinateList:" +coordinateList);
        }


//        @Override
//        protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
//            // https://medium.com/@quiro91/custom-view-mastering-onmeasure-a0a0bb11784d
//        }


//        @Override
//        protected void onAttachedToWindow() {
//            super.onAttachedToWindow();
//            // https://stackoverflow.com/questions/22368632/ondraw-custom-view-inside-a-scrollview
//            setLayerType(LAYER_TYPE_SOFTWARE, null);
//        }
    }



    private class ElementCoordinates {
        int x1;
        int y1;
        int x2;
        int y2;
        int id;
        int parentId;
        String name;
        String post;
        String lineMode;
        int parentRightBound;

        ElementCoordinates(int id, int parentId, String name, String post) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
            this.post = post;
        }

        void setCoordinates(int x1, int y1, int x2, int y2, String lineMode, int parentRightBound){
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.lineMode = lineMode;
            this.parentRightBound = parentRightBound;
        }

        @Override
        public String toString() {
            return "ElementCoordinates{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    ", parentId=" + parentId +
                    ", x1=" + x1 +
                    ", y1=" + y1 +
                    ", x2=" + x2 +
                    ", y2=" + y2 +
                    ", parentRightBound=" + parentRightBound +
                    '}';
        }
    }


    private List<SchemeElement> setElementList(){
//        List<SchemeElement> schemeElements = new ArrayList<>();

//        schemeElements.add(new SchemeElement(10, 0, "Сидоров", "Генеральный директор"));
//        schemeElements.add(new SchemeElement(12, 10, "Иванов", "Технический директор"));
//        schemeElements.add(new SchemeElement(13, 10, "Петров", "Коммерческий директор"));
//        schemeElements.add(new SchemeElement(15, 12, "Фонин", "Зам.тех.директора"));
//        schemeElements.add(new SchemeElement(14, 12, "Зенин", "Главный технолог"));
//        schemeElements.add(new SchemeElement(16, 15, "Мякин", "Главный энергетик"));
//        schemeElements.add(new SchemeElement(17, 15, "Тяпкин", "Главный механик"));
//        schemeElements.add(new SchemeElement(18, 13, "Ляпкин", "Маркетолог"));
//        schemeElements.add(new SchemeElement(19, 12, "Секанов", "Начальник лаборатории"));
//        schemeElements.add(new SchemeElement(20, 17, "Реканов", "Мастер мехслужбы"));
//        schemeElements.add(new SchemeElement(21, 19, "Резанов", "Зам.нач. лаборатории"));


//        schemeElements.add(new SchemeElement(1, 0, "Генеральный директор"));
//        schemeElements.add(new SchemeElement(2, 1, "Технический директор"));
//        schemeElements.add(new SchemeElement(3, 1, "Коммерческий директор"));
//        schemeElements.add(new SchemeElement(5, 2, "Зам.тех.директора"));
//        schemeElements.add(new SchemeElement(4, 2, "Главный технолог"));
//        schemeElements.add(new SchemeElement(6, 5, "Главный энергетик"));
//        schemeElements.add(new SchemeElement(7, 5, "Главный механик"));
//        schemeElements.add(new SchemeElement(8, 3, "Маркетолог"));
//        schemeElements.add(new SchemeElement(9, 2, "Начальник лаборатории"));
//        schemeElements.add(new SchemeElement(10, 7, "Мастер мехслужбы"));
//        schemeElements.add(new SchemeElement(11, 9, "Зам.нач. лаборатории"));
//        schemeElements.add(new SchemeElement(12, 1, "Зам.ген.директора"));
//        schemeElements.add(new SchemeElement(13, 12, "Начальник АХО"));
//        schemeElements.add(new SchemeElement(14, 12, "Начальник отдела кадров"));
//        schemeElements.add(new SchemeElement(15, 4, "Мастер участка-1"));
//        schemeElements.add(new SchemeElement(16, 4, "Мастер участка-2"));
//        schemeElements.add(new SchemeElement(17, 6, "Мастер энергослужбы"));

        //schemeElements.add(new SchemeElement(18, 200, "Мастер-18"));
        //schemeElements.add(new SchemeElement(19, 18, "Мастер-19"));
        return  schemeElements;
    }



//    public class SchemeElement {
//        int id;
//        int parentId;
//        String name;
//        String post;
//
//        public SchemeElement(int id, int parentId, String name, String post) {
//            this.id = id;
//            this.parentId = parentId;
//            this.name = name;
//            this.post = post;
//        }
//
//        @Override
//        public String toString() {
//            return "SchemeElement{" +
//                    "id=" + id +
//                    ", parentId=" + parentId +
//                    ", name='" + name + '\'' +
//                    ", post='" + post + '\'' +
//                    '}';
//        }
//    }


//    private void recursiveTreeConstruction(List<SchemeElement> schemeElements, int parentId){
//        for(SchemeElement element: schemeElements){
//            //Log.e("Rec", "Ищем потомков parentId:" + parentId);
//            //Log.e("Rec", "Текущий элемент:" + element);
//            if (element.parent == parentId) {
//                levelTree++;
//                if (levelTree == 2) {
//                    rootBranchNumber++;
//                }
//                //Log.e("Rec", "Потомок найден, текущий уровень: " + levelTree );
//                calculationCoordinates(element, rootBranchNumber, levelTree);
//                recursiveTreeConstruction(schemeElements, element.id);
//            }
//            //Log.e("Rec", "Потомок не найден, текущий уровень: " + levelTree );
//        }
//        levelTree--; // здесь мы окажемся когда достигли низа текущей ветки
//        //Log.e(TAG, "Конец ветки, текущий уровень: " + levelTree );
//    }
//
//
//    private void calculationCoordinates(SchemeElement element, int rootBranchNumber, int levelTree) {
//        int x1 = 100, y1 = 0, x2 = 400, y2 = 50;
//        int branchOffset = 0;
//        String lineMode = "root";
//        int countBrothers = 0;
//
////        ElementCoordinates elementCoordinates = new ElementCoordinates(element.id, element.parent, element.name);
////        elementCoordinates.setCoordinates(0,0,0,0, "simple", rootBranchNumber);
////        coordinateList.add(elementCoordinates);
////        int currentIndex = coordinateList.indexOf(elementCoordinates);
//        // добавляем чтобы учесть элемент который еще не добавлен в список
//        // (будет добавлен в после цикла) чтобы исходя из его номера ветки получить смепщение по предыдущей ветке
//        // это создает такую проблему: этот элемент учитвается при расчете братских элементов, и по всей схеме смещение.
////        MySysUtil.PrintList<ElementCoordinates> mySysUtil = new MySysUtil.PrintList<>();
////        mySysUtil.executePrint(coordinateList, "List-before: ");
//
//        for (ElementCoordinates data: coordinateList){
//
//            // get parent coordinates  // ищем в существующем списке координат координаты родителя текущего элемента
//            if (element.parent == data.id){
//                x1 = data.x1;
//                y1 = data.y1;
//                x2 = data.x2;
//                y2 = data.y2;
//                lineMode = "simple";
//            }
//
////            if (data.rootBranchNumber > 1){ // нам нужны границы левой ветки начиная со второй ветки
////                branchOffset = defineBoundariesPreviousBranch(data.rootBranchNumber - 1);
////            }
//
//
//            if (levelTree == 2 & rootBranchNumber > 1 & data.rootBranchNumber != rootBranchNumber){ // нам нужны границы левой ветки начиная со второй ветки
//                branchOffset = defineBoundariesPreviousBranch(rootBranchNumber - 1);
//                lineMode = "combine";
//            }
//
//            // count brothers (нужно в пределах одной ветви считать братьев)
//            if (element.parent == data.parent & data.rootBranchNumber == rootBranchNumber){
//                countBrothers ++;
//                lineMode = "combine";
//            }
//        }
//
//        //coordinateList.remove(currentIndex);
//
//        int brothersOffset = countBrothers * 400;
//        ElementCoordinates elementCoordinates = new ElementCoordinates(element.id, element.parent, element.name);
//        elementCoordinates.setCoordinates(x1 + brothersOffset + branchOffset, y1 + 150,
//                x2 + brothersOffset + branchOffset, y2 + 150, lineMode, rootBranchNumber, x2);
//        Log.e("E", "C:" + elementCoordinates);
//        coordinateList.add(elementCoordinates);
//        //mySysUtil.executePrint(coordinateList, "List-after: ");
//    }


    //    private int defineBoundariesPreviousBranch(int previousBranchNumber){
//        int bound = 0;
//        for (ElementCoordinates data: coordinateList){
//            if (data.rootBranchNumber == previousBranchNumber){
//                bound = data.x2;
//            }
//        }
//        //Log.e(TAG, "Return Bound: " + bound);
//        return bound;
//    }
//
//
//    private void defineBoundariesAllBranch(){
//        int localBranchNumber = 0;
//        for (ElementCoordinates data: coordinateList){
//            if (data.rootBranchNumber == localBranchNumber){
//                //Log.e(TAG, "Equals: " + data.rootBranchNumber);
//                branchBoundaries.put(localBranchNumber, data.x2);
//            } else {
//                //Log.e(TAG, "NoEquals: "+ data.rootBranchNumber);
//                localBranchNumber = data.rootBranchNumber;
//                branchBoundaries.put(localBranchNumber, data.x2);
//            }
//        }
//    }


}
