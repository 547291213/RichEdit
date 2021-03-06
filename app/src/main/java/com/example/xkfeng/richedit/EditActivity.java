package com.example.xkfeng.richedit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xkfeng.richedit.JavaBean.EditSql;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.github.mthli.knife.KnifeText;

/**
 * Created by initializing on 2018/5/18.
 */

public class EditActivity extends AppCompatActivity{

    private static final int USER_ID_DATA = 1 ;
    private static final String TAG = "EditActivity" ;
    private TextView editingText , backText , finishText ;
    private static int id = 1 ;
//    private EditText editTitle ;

//    private static final String BOLD = "<b>Bold</b><br><br>";
//    private static final String ITALIT = "<i>Italic</i><br><br>";
//    private static final String UNDERLINE = "<u>Underline</u><br><br>";
//    private static final String STRIKETHROUGH = "<s>Strikethrough</s><br><br>"; // <s> or <strike> or <del>
//    private static final String BULLET = "<ul><li>asdfg</li></ul>";
//    private static final String QUOTE = "<blockquote>Quote</blockquote>";
//    private static final String LINK = "<a href=\"https://github.com/mthli/Knife\">Link</a><br><br>";
//    private static final String EXAMPLE = BOLD + ITALIT + UNDERLINE + STRIKETHROUGH + BULLET + QUOTE + LINK;

    private KnifeText knife;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_layout);
        /*
        隐藏顶部标题栏
         */
        ActionBar actionBar = getSupportActionBar() ;
        if (actionBar != null)
        {
            actionBar.hide();
        }


        editingText = (TextView)findViewById(R.id.editingText) ;
        backText = (TextView)findViewById(R.id.backText) ;
        finishText = (TextView)findViewById(R.id.finishText);

        MyClick myClick = new MyClick();
        editingText.setOnClickListener(myClick);
        backText.setOnClickListener(myClick);
        finishText.setOnClickListener(myClick);
       // editTitle = findViewById(R.id.editTitle) ;

        knife = (KnifeText) findViewById(R.id.knife);

        // ImageGetter coming soon...
       // knife.fromHtml(EXAMPLE);
        knife.setSelection(knife.getEditableText().length());

        /*
        如果是修改数据，则需要显示当前列表项的数据
         */
        if (getIntent().getStringExtra("data") != null)
        {
            knife.setText(Html.fromHtml(getIntent().getStringExtra("data"))) ;


        }
        setupBold();
        setupItalic();
        setupUnderline();
        setupStrikethrough();
        setupBullet();
        setupQuote();
        setupLink();
        setupClear();

    }

    public class MyClick implements View.OnClickListener {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.finishText) {
              /*
              获取输入的内容载入数据库
               */
                //如果是直接添加内容
                if (MainActivity.EDIT_STATE == 1)
                {   String data = knife.getText().toString() ;
                    Log.i(TAG , "THE DTAT IS "+ data) ;
                    String title = data.split("\n")[0] ;

                    Log.i(TAG , "THE TITLE IS "+  knife.toHtml()) ;
                    EditSql editSql = new EditSql();
                    editSql.setCollected(false);
                    editSql.setId(id);
                    editSql.setContent(knife.toHtml());
                    editSql.setCreate_time(getTime());
                    editSql.setUpdate_time(getTime());
                    editSql.setOrigine_content(knife.getText().toString());
                    editSql.setTitle(title);
                    editSql.setUser_id(1);
                    editSql.save();

                    //id增加
                    id++ ;
                    Intent intent = new Intent() ;
                    intent.putExtra("action" ,"homeFragment") ;
                    intent.setAction("com.example.xkfeng.richedit.mainbroadcast") ;
                    sendBroadcast(intent);

                }
                //如果是对现有内容进行修改
                else if (MainActivity.EDIT_STATE == 2)
                {
                     Log.i(TAG , "EDIT IS UPDATE") ;
                     String data = knife.getText().toString() ;
                     String title = data.split("\n")[0] ;
                     EditSql editSql = new EditSql();
                     editSql.setContent(knife.toHtml());
                     editSql.setOrigine_content(knife.getText().toString());
                     editSql.setTitle(title);
                     editSql.setUpdate_time(getTime());
                     editSql.update(getIntent().getIntExtra("id" , 0)) ;

                    Intent intent = new Intent() ;
                    if (MainActivity.CURRENT_PAGE == 1)
                    {
                        intent.putExtra("action" ,"homeFragment") ;
                    }else if(MainActivity.CURRENT_PAGE == 2)
                    {
                        intent.putExtra("action" ,"collectionFragment") ;
                    }
                    intent.setAction("com.example.xkfeng.richedit.mainbroadcast") ;
                    sendBroadcast(intent);
                }

                //退出当前Activity
                finish();
            }

            else if (v.getId() == R.id.backText)
            {
                Log.i(TAG , "点击了back按钮") ;
                EditActivity.this.finish();
              
            }
            else if (v.getId() == R.id.editingText)
            {

            }
            Toast.makeText(EditActivity.this , "OnClick" , Toast.LENGTH_SHORT).show();
        }
    }

    public static String getTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        Date date = new Date() ;
        String str = sdf.format(date) ;
        return str ;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(EditActivity.this)
                .setIcon(R.drawable.app_pic)
                .setTitle("退出")
                .setMessage("退出将不会保存您编辑的数据")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditActivity.this.finish();
                    }
                })
                .setNegativeButton("取消",null)
                .create()
                .show();
    }

    private void setupBold() {
        ImageButton bold = (ImageButton) findViewById(R.id.bold);

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.bold(!knife.contains(KnifeText.FORMAT_BOLD));
            }
        });

        bold.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(EditActivity.this, R.string.toast_bold, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupItalic() {
        ImageButton italic = (ImageButton) findViewById(R.id.italic);

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.italic(!knife.contains(KnifeText.FORMAT_ITALIC));
            }
        });

        italic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(EditActivity.this, R.string.toast_italic, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupUnderline() {
        ImageButton underline = (ImageButton) findViewById(R.id.underline);

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.underline(!knife.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });

        underline.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(EditActivity.this, R.string.toast_underline, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupStrikethrough() {
        ImageButton strikethrough = (ImageButton) findViewById(R.id.strikethrough);

        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.strikethrough(!knife.contains(KnifeText.FORMAT_STRIKETHROUGH));
            }
        });

        strikethrough.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(EditActivity.this, R.string.toast_strikethrough, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupBullet() {
        ImageButton bullet = (ImageButton) findViewById(R.id.bullet);

        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.bullet(!knife.contains(KnifeText.FORMAT_BULLET));
            }
        });


        bullet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(EditActivity.this, R.string.toast_bullet, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupQuote() {
        ImageButton quote = (ImageButton) findViewById(R.id.quote);

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.quote(!knife.contains(KnifeText.FORMAT_QUOTE));
            }
        });

        quote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(EditActivity.this, R.string.toast_quote, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupLink() {
        ImageButton link = (ImageButton) findViewById(R.id.link);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkDialog();
            }
        });

        link.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(EditActivity.this, R.string.toast_insert_link, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupClear() {
        ImageButton clear = (ImageButton) findViewById(R.id.clear);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.clearFormats();
            }
        });

        clear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(EditActivity.this, R.string.toast_format_clear, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void showLinkDialog() {
        final int start = knife.getSelectionStart();
        final int end = knife.getSelectionEnd();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = (EditText) view.findViewById(R.id.edit);
        builder.setView(view);
        builder.setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = editText.getText().toString().trim();
                if (TextUtils.isEmpty(link)) {
                    return;
                }

                // When KnifeText lose focus, use this method
                knife.link(link, start, end);
            }
        });

        builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // DO NOTHING HERE
            }
        });

        builder.create().show();
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.undo:
//                knife.undo();
//                break;
//            case R.id.redo:
//                knife.redo();
//                break;
//            case R.id.github:
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.app_repo)));
//                startActivity(intent);
//                break;
//            default:
//                break;
//        }
//
//        return true;
//    }
}
