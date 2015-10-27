###Adapter：
* IBaseAdapter
* extends `BaseAdapter`:
    * AbsBaseAdapter
* extends `RecyclerView.Adapter`:
    * BaseRecyclerAdapter
    * CardAdapter

1、when you use ListView in your layout,you should use the adapter which extends `AbsBaseAdapter`. 
eg：
```java
public class TestAbsAdapter extends AbsBaseAdapter {

    public TestAbsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutID() {
        return R.layout.test_my_text_view;
    }

    @Override
    public void onBindViewHolder(int position, ViewHolder holder, ViewGroup parent) {
        TextView textView = (TextView) holder.getView(R.id.test_tv);
        textView.setText(position+"");
    }
}
//Anonymous Inner Class
AbsBaseAdapter mAdapter = new AbsBaseAdapter(getActivity()) {
            @Override
            public int getLayoutID() {
                return  R.layout.test_my_text_view;
            }

            @Override
            public void onBindViewHolder(int position, ViewHolder holder, ViewGroup parent) {
                TextView textView = (TextView) holder.getView(R.id.test_tv);
                textView.setText(position+"");
            }
        };
```
2、Usually when using Recyclerview using adapter which extends `BaseRecyclerAdapter`.
eg:
```java
public class TestRecylcerAdapter extends BaseRecyclerAdapter<JSONObject>{
   
   // init ViewHolder and layout
    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup viewGroup, int type) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test_my_text_view,viewGroup,false);
        return new BaseRecyclerHolder(v);
    }

    // bind data
    @Override
    public void onBindData(RecyclerView.ViewHolder viewHolder, int position) {
        TextView textView = ((BaseRecyclerHolder) viewHolder).getView(R.id.test_tv);
        textView.setText("位置是:"+position);
    }

}
```

3、if you want to use multiple type itemview in your layout with RecyclerView,you should use `CardAdapter`. and also,you should create a `BaseCard` before using `CardAdapter`.
```java
public class TestTextCard extends BaseCard {

    TextView mtextView;

    public TestTextCard(View itemView){
        super(itemView);
    }

    @Override
    public int getCardLayout() {
        return R.layout.test_item;
    }

    @Override
    public void initView() {
        mtextView = (TextView)itemView.findViewById(R.id.test_tv);
    }

    @Override
    public void onBindData(Object data, int position) {
        mtextView.setText("position:"+position);
    }
    
    //you can reset data or state there.
    @Override
    public void onViewDetachedFromWindow() {

    }
}
```

```java
public class TestCardAdapter extends CardAdapter {
    
    /**
     * @param position 
     * @return the position of card in the mCardList.
     */
    @Override
    public int getCardType(int position) {
        return  (position % 2 == 0 ? 1 : 0);
    }

    /**
     *  setup the card that you want to load
     * @param mCardList
     */
    @Override
    public void setupCardList(ArrayList mCardList) {
        mCardList.add(TestTextCard.class);
        mCardList.add(TestImageCard.class);
    }
}

```