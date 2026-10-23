package cn.edu.sicnu.stu.zhangyuxin.fourth;

import android.app.Activity;
import android.content.Intent;
import android.widget.*;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import cn.edu.sicnu.stu.zhangyuxin.fourth.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AppUiTest {
 private android.app.Instrumentation instrumentation() { return InstrumentationRegistry.getInstrumentation(); }
 private <T extends Activity> T launch(Class<T> cls) {
  Intent intent=new Intent(instrumentation().getTargetContext(),cls).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  return cls.cast(instrumentation().startActivitySync(intent));
 }
 private void ui(Runnable action) { instrumentation().runOnMainSync(action); instrumentation().waitForIdleSync(); }

 @Test public void profileSaveUpdatesHomeAndCancelDoesNotSave() {
  MainActivity home=launch(MainActivity.class);
  ProfileActivity edit=launch(ProfileActivity.class);
  ui(() -> {
   ((EditText)edit.findViewById(R.id.input_phone)).setText("13800138000");
   ((EditText)edit.findViewById(R.id.input_email)).setText("zhangyuxin@example.com");
   ((RadioButton)edit.findViewById(R.id.female)).setChecked(true);
   edit.findViewById(R.id.save).performClick();
  });
  assertEquals("13800138000",Player.phone);
  ui(() -> assertTrue(((TextView)home.findViewById(R.id.value_phone)).getText().toString().contains("13800138000")));
  ProfileActivity cancel=launch(ProfileActivity.class);
  ui(() -> { ((EditText)cancel.findViewById(R.id.input_phone)).setText("13900139000"); cancel.findViewById(R.id.cancel).performClick(); });
  assertEquals("13800138000",Player.phone);
  ui(home::finish);
 }
 @Test public void requiredFieldsRejectEmptyName() {
  ProfileActivity edit=launch(ProfileActivity.class);
  ui(() -> { ((EditText)edit.findViewById(R.id.input_name)).setText(""); edit.findViewById(R.id.save).performClick();
   assertNotNull(((EditText)edit.findViewById(R.id.input_name)).getError()); assertFalse(edit.isFinishing()); edit.finish(); });
 }
 @Test public void cardInteractionScoreLockResetAndBest() {
  GameActivity activity=launch(GameActivity.class);
  ui(() -> {
   List<Card> cards=new ArrayList<>(); cards.add(new Card("♥","A",false,false)); cards.add(new Card("♣","A",false,false));
   for(int i=2;i<16;i++) cards.add(new Card(i%2==0?"♦":"♠",Integer.toString(i),false,false));
   activity.setGameForTest(new CardMatchingGame(cards));
   GridLayout board=activity.findViewById(R.id.board);
   board.getChildAt(0).performClick(); assertEquals(-1,activity.getGameForTest().getScore());
   board.getChildAt(1).performClick(); assertEquals(14,activity.getGameForTest().getScore());
   assertFalse(board.getChildAt(0).isEnabled()); assertEquals(14,Player.best);
   activity.findViewById(R.id.reset).performClick();
   assertEquals(0,activity.getGameForTest().getScore()); assertEquals(0,activity.getGameForTest().getMatchedCount());
   for(int i=0;i<16;i++) assertTrue(board.getChildAt(i).isEnabled());
   assertEquals(14,Player.best); activity.findViewById(R.id.exit).performClick(); assertTrue(activity.isFinishing());
  });
 }
 @Test public void mismatchTurnsPreviousCardBack() {
  GameActivity activity=launch(GameActivity.class);
  ui(() -> {
   List<Card> cards=new ArrayList<>(); for(int i=0;i<16;i++) cards.add(new Card(i%2==0?"♥":"♣",Integer.toString(i),false,false));
   activity.setGameForTest(new CardMatchingGame(cards)); GridLayout board=activity.findViewById(R.id.board);
   board.getChildAt(0).performClick(); board.getChildAt(1).performClick();
   assertEquals(-4,activity.getGameForTest().getScore());
   assertEquals(activity.getString(R.string.card_back),((TextView)board.getChildAt(0)).getText().toString());
   activity.finish();
  });
 }
}
