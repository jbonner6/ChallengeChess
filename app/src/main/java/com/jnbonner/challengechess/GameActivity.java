package com.jnbonner.challengechess;

import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;

import com.jnbonner.challengechess.Model.GameBoard.Color;
import com.jnbonner.challengechess.Model.GameBoard.GameBoard;
import com.jnbonner.challengechess.Model.GameBoard.GameBoardAdapter;
import com.jnbonner.challengechess.Model.GameBoardFragment;
import com.jnbonner.challengechess.Model.playerInfoFragment;

public class GameActivity extends AppCompatActivity {

    private FragmentManager fm;
    private GameBoardFragment gameBoardFragment;
    private DrawerLayout drawerLayout;
    private boolean isSingle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        isSingle = getIntent().getExtras().getBoolean("isSingle");
        fm = this.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.board_frame_holder);
        if (fragment == null){
            fragment = createFragment();
            fm.beginTransaction().add(R.id.board_frame_holder, fragment).commit();
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSingle", isSingle);
        fragment.setArguments(bundle);

        drawerLayout = (DrawerLayout)findViewById(R.id.boardDrawerLayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        final Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.black_bishop, getTheme());
        actionBarDrawerToggle.setHomeAsUpIndicator(drawable);
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }


    private Fragment createFragment(){
        gameBoardFragment = new GameBoardFragment();
        return gameBoardFragment;
    }

    public GameBoardFragment getGameBoardFragment() {
        return gameBoardFragment;
    }

    public void setGameBoardFragment(GameBoardFragment gameBoardFragment) {
        this.gameBoardFragment = gameBoardFragment;
    }

}
