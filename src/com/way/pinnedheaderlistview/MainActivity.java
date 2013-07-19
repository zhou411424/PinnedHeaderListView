package com.way.pinnedheaderlistview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.way.view.BladeView;
import com.way.view.BladeView.OnItemClickListener;
import com.way.view.PinnedHeaderListView;

public class MainActivity extends Activity {
	private static final String FORMAT = "^[a-z,A-Z].*$";
	private PinnedHeaderListView mListView;
	private BladeView mLetter;
	private FriendsAdapter mAdapter;
	private String[] datas;
	// 首字母集
	private List<String> mSections;
	// 根据首字母存放数据
	private Map<String, List<String>> mMap;
	// 首字母位置集
	private List<Integer> mPositions;
	// 首字母对应的位置
	private Map<String, Integer> mIndexer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		initView();
	}

	private void initData() {
		datas = getResources().getStringArray(R.array.countries);
		mSections = new ArrayList<String>();
		mMap = new HashMap<String, List<String>>();
		mPositions = new ArrayList<Integer>();
		mIndexer = new HashMap<String, Integer>();

		for (int i = 0; i < datas.length; i++) {
			String firstName = datas[i].substring(0, 1);
			if (firstName.matches(FORMAT)) {
				if (mSections.contains(firstName)) {
					mMap.get(firstName).add(datas[i]);
				} else {
					mSections.add(firstName);
					List<String> list = new ArrayList<String>();
					list.add(datas[i]);
					mMap.put(firstName, list);
				}
			} else {
				if (mSections.contains("#")) {
					mMap.get("#").add(datas[i]);
				} else {
					mSections.add("#");
					List<String> list = new ArrayList<String>();
					list.add(datas[i]);
					mMap.put("#", list);
				}
			}
		}

		Collections.sort(mSections);
		int position = 0;
		for (int i = 0; i < mSections.size(); i++) {
			mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
			mPositions.add(position);// 首字母在listview中位置，存入list中
			position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		mListView = (PinnedHeaderListView) findViewById(R.id.friends_display);
		mLetter = (BladeView) findViewById(R.id.friends_myletterlistview);
		mLetter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(String s) {
				if (mIndexer.get(s) != null) {
					mListView.setSelection(mIndexer.get(s));
				}
			}
		});
		mAdapter = new FriendsAdapter(this, datas, mSections, mPositions);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(mAdapter);
		mListView.setPinnedHeaderView(LayoutInflater.from(this).inflate(
				R.layout.listview_head, mListView, false));
	}

}
