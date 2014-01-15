package com.raul.basic.android.ui.basic;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.raul.basic.android.R;
import com.raul.basic.android.ui.basic.actionbar.ActionBarMenuItem;
import com.raul.basic.android.ui.basic.actionbar.BasicActionBarActivity;
import com.raul.basic.android.util.FileUtil;
import com.raul.basic.android.util.Log;

/**
 * 文件管理器
 * 
 * @author xiaomiaomiao
 * 
 */
public class ExplorerActivity extends BasicActionBarActivity {
	/**
	 * Debug-TAG
	 */
	private static final String TAG = "ExplorerActivity";

	/**
	 * 存放文件路径和图标的Map的键值 Map_title
	 */
	private static final String MAP_TITLE = "title";

	/**
	 * 存放文件路径和图标的Map的键值 Map_info
	 */
	private static final String MAP_INFO = "info";

	/**
	 * 存放文件路径和图标的Map的键值 Map_img
	 */
	private static final String MAP_IMG = "img";

	/**
	 * 存放文件路径和图标的Map的键值 Map_infofiles
	 */
	private static final String MAP_INFOFILES = "infofiles";

	/**
	 * 发送文件按钮
	 */
	private static final int MENU_SEND_FILE = 0;

	/**
	 * 从SD卡上获取的所有数据
	 */
	private List<Map<String, Object>> mData;

	/**
	 * SD卡的路径
	 */
	private String mDir = Environment.getExternalStorageDirectory()
			.getAbsolutePath();

	/**
	 * ListView用于显示文件列表
	 */
	private ListView lv;

	/**
	 * 选中的文件的Uri
	 */
	private String mFileUri;

	/**
	 * listView的数据MyAdapter
	 */
	private MyAdapter mAdapter;

	/**
	 * 
	 * 按下返回键的操作
	 * 
	 * @param keyCode
	 *            按键标识
	 * @param event
	 *            按键事件
	 * @return boolean true
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return fileBack() ? true : super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int onCreateMenu(ArrayList<ActionBarMenuItem> menus) {
		menus.add(new ActionBarMenuItem(MENU_SEND_FILE,
				R.drawable.btn_hook_selector));
		return super.onCreateMenu(menus);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onMenuClick(int menuId) {
		if (MENU_SEND_FILE == menuId) {
			Log.i(TAG, "ExplorerActivity fileUri--------->" + mFileUri);
			finishWithResult();
		}
	}

	/**
	 * 
	 * Activity onCreate方法 初始化页面信息
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLogo(R.drawable.icon_chat_white);
		setActionBarTitle("Choose file");
		setMenuEnabled(LEFT, false);
		mData = getData();

		lv = (ListView) findViewById(R.id.list);
		mAdapter = new MyAdapter(this);
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if ((Integer) mData.get(position).get(MAP_IMG) != null) {
					if ((Integer) mData.get(position).get(MAP_IMG) == R.drawable.icon_filemanager_folder
							|| (Integer) mData.get(position).get(MAP_IMG) == R.drawable.btn_filemanager_back) {
						mDir = (String) mData.get(position).get(MAP_INFO);
						Log.i(TAG, "onItemClick-----mDir-------------->" + mDir);
						fileNext();
						mAdapter.notifyDataSetChanged();
						// 重新加载Adapter时将数据置为null
						mFileUri = null;
					} else {
						// 选中，则置为未选中
						if (isSelected((String) mData.get(position).get(
								MAP_INFO))) {
							mFileUri = null;
							setMenuEnabled(LEFT, false);
						} else {
							mFileUri = (String) mData.get(position).get(
									MAP_INFO);
							setMenuEnabled(LEFT, true);
						}
						mAdapter.notifyDataSetChanged();
					}
				}
			}
		});
	}

	/**
	 * 返回当前Activity布局的layoutId 用于显示当前页面的布局
	 * 
	 */
	@Override
	protected int getViewId() {
		return R.layout.explorer_list;
	}

	/**
	 * 
	 * 获取SD卡上的文件数据
	 * 
	 * @return List SD卡上的文件目录
	 */
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		File f = new File(mDir);
		Log.i(TAG, "mDir=---------------------->" + mDir);
		File[] files = f.listFiles();
		sortByType(files);
		map = new HashMap<String, Object>();
		map.put(MAP_TITLE, mDir);
		map.put(MAP_INFO, f.getParent());
		map.put(MAP_IMG, R.drawable.btn_filemanager_back);
		list.add(map);
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				map = new HashMap<String, Object>();
				map.put(MAP_TITLE, files[i].getName());
				map.put(MAP_INFO, files[i].getPath());
				// 目录
				if (files[i].isDirectory()) {
					if (files[i].listFiles() != null) {
						map.put(MAP_INFOFILES, files[i].listFiles().length);
					} else {
						map.put(MAP_INFOFILES, 0);
					}
					map.put(MAP_IMG, R.drawable.icon_filemanager_folder);
				} else if (files[i].isFile()) {
					// 文件
					// 获取文件名，判断类型，设置图片
					String fileName = FileUtil.getFileNameWithSuffix(files[i]
							.getPath());
					String fileType = FileUtil.getMIMEType(fileName);
					Log.i(TAG, "fileType====---------------------->" + fileType);
					if (fileType.equals("text/plain")) {
						// txt
						map.put(MAP_IMG,
								R.drawable.icon_filemanager_filetype_txt);
					} else if (fileType.equals("audio/x-mpeg")
							|| fileType.equals("audio/x-mpegurl")
							|| fileType.equals("audio/mp4a-latm")
							|| fileType.equals("audio/mp4a-latm")
							|| fileType.equals("audio/mp4a-latm")
							|| fileType.equals("audio/x-mpeg")
							|| fileType.equals("audio/x-mpeg")
							|| fileType.equals("audio/mpeg")
							|| fileType.equals("audio/x-pn-realaudio")
							|| fileType.equals("audio/x-wav")
							|| fileType.equals("audio/x-ms-wma")
							|| fileType.equals("audio/x-ms-wmv")) {
						// 音频
						map.put(MAP_IMG,
								R.drawable.icon_filemanager_filetype_audio);
					} else if (fileType.equals("text/html")) {
						// html
						map.put(MAP_IMG,
								R.drawable.icon_filemanager_filetype_html);
					} else if (fileType.equals("application/pdf")) {
						// pdf
						map.put(MAP_IMG,
								R.drawable.icon_filemanager_filetype_pdf);
					} else if (fileType.equals("image/png")
							|| fileType.equals("image/jpeg")
							|| fileType.equals("image/bmp")
							|| fileType.equals("image/gif")) {
						// image
						map.put(MAP_IMG,
								R.drawable.icon_filemanager_filetype_picture);
					} else if (fileType.equals("application/vnd.ms-powerpoint")
							|| fileType
									.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")) {
						// ppt
						map.put(MAP_IMG,
								R.drawable.icon_filemanager_filetype_ppt);
					} else if (fileType.equals("application/x-zip-compressed")
							|| fileType.equals("application/x-compress")) {
						// rar
						map.put(MAP_IMG,
								R.drawable.icon_filemanager_filetype_rar);
					} else if (fileType.equals("video/mp4")
							|| fileType.equals("video/3gpp")
							|| fileType.equals("video/x-ms-asf")
							|| fileType.equals("video/x-msvideo")
							|| fileType.equals("video/vnd.mpegurl")
							|| fileType.equals("video/x-m4v")
							|| fileType.equals("video/quicktime")
							|| fileType.equals("video/mpeg")
							|| fileType.equals("video/mpeg")
							|| fileType.equals("video/mpeg")) {
						// video
						map.put(MAP_IMG,
								R.drawable.icon_filemanager_filetype_video);
					} else if (fileType.equals("application/vnd.ms-excel")
							|| fileType
									.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
						// xls
						map.put(MAP_IMG,
								R.drawable.icon_filemanager_filetype_xls);
					} else if (fileType
							.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
							|| fileType.equals("application/msword")) {
						// word
						map.put(MAP_IMG,
								R.drawable.icon_filemanager_filetype_word);
					} else {
						map.put(MAP_IMG,
								R.drawable.icon_filemanager_filetype_other);
					}
				}
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 选择完成，获得文件的Uri
	 */
	private void finishWithResult() {
		if (!TextUtils.isEmpty(mFileUri)) {
			Intent intent = new Intent();
			Uri startDir = Uri.fromFile(new File(mFileUri));
			intent.setDataAndType(startDir,
					"vnd.android.cursor.dir/lysesoft.andexplorer.file");
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	/**
	 * 
	 * 返回上层目录
	 * 
	 * @return boolean用于onKeyDown返回
	 */
	private boolean fileBack() {
		if (!mDir.equals(Environment.getExternalStorageDirectory()
				.getAbsolutePath())) {
			mDir = new File(mDir).getParent();
			mData = getData();
			mAdapter.notifyDataSetChanged();
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 进入下层目录
	 */
	private void fileNext() {
		Log.i(TAG, "onItemClick-----mDir-------------->" + mDir);
		mData = getData();

		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 判断ListViewItem是否选中
	 */
	private boolean isSelected(String fileUri) {
		return (null != mFileUri) ? fileUri.equals(mFileUri) : false;
	}

	/**
	 * 
	 * ViewHolder
	 * 
	 */
	public final class ViewHolder {
		/**
		 * 文件夹和文件的布局
		 */
		public LinearLayout itemFile;

		/**
		 * 目录的布局
		 */
		public LinearLayout itemTitle;

		/**
		 * img的Layout
		 */
		public LinearLayout imgLayout;

		/**
		 * 文件图标
		 */
		public ImageView img;

		/**
		 * 目录的图片
		 */
		public ImageView imgTitle;

		/**
		 * 目录的内容
		 */
		public TextView titleTitle;

		/**
		 * 文件名
		 */
		public TextView title;

		/**
		 * 子文件数
		 */
		public TextView infofiles;

		/**
		 * 文件夹目录右边的>图标
		 */
		public ImageView arrow;

		/**
		 * 文件目录右边的\/图标
		 */
		public ImageView hook;

	}

	/**
	 * 
	 * MyAdapter 用于展现ListView的数据
	 * 
	 */
	public class MyAdapter extends BaseAdapter {
		/**
		 * LayoutInflater
		 */
		private LayoutInflater mInflater;

		/**
		 * 
		 * 构造方法
		 * 
		 * @param context
		 *            Context
		 */
		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		/**
		 * 
		 * 获取存放文件List的个数
		 * 
		 * @return int 文件个数
		 * @see android.widget.Adapter#getCount()
		 */
		public int getCount() {
			return mData.size();
		}

		/**
		 * 
		 * 获取Item上的对象
		 * 
		 * @param arg0
		 *            position
		 * @return Object Object
		 * @see android.widget.Adapter#getItem(int)
		 */
		public Object getItem(int arg0) {
			return null;
		}

		/**
		 * 
		 * 获取ItemId
		 * 
		 * @param arg0
		 *            position
		 * @return ItemId
		 * @see android.widget.Adapter#getItemId(int)
		 */
		public long getItemId(int arg0) {
			return arg0;
		}

		/**
		 * 
		 * ListView Item是否可点击
		 * 
		 * @param position
		 *            位置
		 * @return boolean true可点击
		 * @see android.widget.BaseAdapter#isEnabled(int)
		 */
		public boolean isEnabled(int position) {
			if ((Integer) mData.get(position).get(MAP_IMG) == R.drawable.btn_filemanager_back
					&& ((String) mData.get(position).get(MAP_TITLE))
							.equals(Environment.getExternalStorageDirectory()
									.getAbsolutePath())) {
				return false;
			}
			return true;
		}

		/**
		 * 
		 * 自定义AdapterView
		 * 
		 * @param position
		 *            位置
		 * @param convertView
		 *            当前View
		 * @param parent
		 *            ViewGroup
		 * @return View
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.explorer_list_item_all, null);
				holder.itemFile = (LinearLayout) convertView
						.findViewById(R.id.item_file_layout);
				holder.itemTitle = (LinearLayout) convertView
						.findViewById(R.id.item_title_layout);
				holder.imgLayout = (LinearLayout) convertView
						.findViewById(R.id.img_layout);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.imgTitle = (ImageView) convertView
						.findViewById(R.id.img_title);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.titleTitle = (TextView) convertView
						.findViewById(R.id.title_title);
				holder.infofiles = (TextView) convertView
						.findViewById(R.id.infofiles);
				holder.arrow = (ImageView) convertView.findViewById(R.id.arrow);
				holder.hook = (ImageView) convertView
						.findViewById(R.id.hook_selected);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (null != (Integer) mData.get(position).get(MAP_IMG)) {
				// dir
				if ((Integer) mData.get(position).get(MAP_IMG) == R.drawable.btn_filemanager_back) {
					Log.i(TAG, "icon file back mDir--------------->" + mDir);
					holder.itemFile.setVisibility(View.GONE);
					holder.itemTitle.setVisibility(View.VISIBLE);
					if (mDir.equals(Environment.getExternalStorageDirectory()
							.getAbsolutePath())) {
						holder.imgTitle
								.setImageResource(R.drawable.btn_filemanager_back_disable);
					} else {
						holder.imgTitle
								.setImageResource(R.drawable.btn_filemanager_back_normal);
					}
					holder.titleTitle.setText((String) mData.get(position).get(
							MAP_TITLE));
					holder.infofiles.setVisibility(View.GONE);
					holder.arrow.setVisibility(View.GONE);
				} else if ((Integer) mData.get(position).get(MAP_IMG) == R.drawable.icon_filemanager_folder) {
					holder.itemTitle.setVisibility(View.GONE);
					holder.itemFile.setVisibility(View.VISIBLE);
					// 文件夹要显示内部文件个数
					holder.img.setImageResource((Integer) mData.get(position)
							.get(MAP_IMG));

					holder.title.setText((String) mData.get(position).get(
							MAP_TITLE));
					holder.infofiles.setVisibility(View.VISIBLE);
					holder.arrow.setVisibility(View.VISIBLE);
					holder.infofiles.setText(getResources().getString(
							R.string.file_number_unit,
							String.valueOf(mData.get(position).get(
									MAP_INFOFILES))));
					holder.arrow
							.setImageResource(R.drawable.icon_arrow_right_black_big);
				} else {
					holder.itemFile.setVisibility(View.VISIBLE);
					holder.itemTitle.setVisibility(View.GONE);
					holder.img.setImageResource((Integer) mData.get(position)
							.get(MAP_IMG));
					holder.infofiles.setVisibility(View.GONE);
					holder.arrow.setVisibility(View.GONE);

					holder.title.setText((String) mData.get(position).get(
							MAP_TITLE));
				}

			}
			holder.title.setText((String) mData.get(position).get(MAP_TITLE));

			// 设置选中和未选中的背景

			holder.hook.setVisibility(isSelected((String) mData.get(position)
					.get(MAP_INFO)) ? View.VISIBLE : View.GONE);
			return convertView;
		}
	}

	/**
	 * 
	 * 对文件进行排序
	 * 
	 * @param files
	 *            File[]
	 */
	private void sortByType(File[] files) {
		if (null != files && files.length > 0) {
			Arrays.sort(files, new Comparator<File>() {

				@Override
				public int compare(File file1, File file2) {
					if (file1.isDirectory()) {
						if (file2.isFile()) {
							return -1;
						} else {
							return (file1.getName().toUpperCase())
									.compareTo(file2.getName().toUpperCase());
						}
					} else {
						if (file2.isDirectory()) {
							return 1;
						} else {
							return (file1.getName().toUpperCase())
									.compareTo(file2.getName().toUpperCase());
						}
					}

				}

			});
		}
	}
}
