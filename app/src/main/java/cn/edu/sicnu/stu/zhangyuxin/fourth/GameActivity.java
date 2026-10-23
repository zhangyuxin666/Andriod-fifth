package cn.edu.sicnu.stu.zhangyuxin.fourth;
import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import cn.edu.sicnu.stu.zhangyuxin.fourth.model.*;
public class GameActivity extends Activity {
 private CardMatchingGame game;
 private final TextView[] cards=new TextView[16];
 private String hint;
 @Override public void onCreate(Bundle state) { super.onCreate(state); Player.init(this); setContentView(R.layout.activity_game);
  game=state==null?new CardMatchingGame(16):(CardMatchingGame)state.getSerializable(getString(R.string.key_model));
  hint=state==null?getString(R.string.initial_hint):state.getString(getString(R.string.key_hint));
  GridLayout grid=findViewById(R.id.board);
  for(int i=0;i<16;i++) {
   final int index=i; TextView card=new TextView(this); cards[i]=card;
   GridLayout.LayoutParams lp=new GridLayout.LayoutParams(GridLayout.spec(i/4,1f),GridLayout.spec(i%4,1f));
   lp.width=0; lp.height=0; int margin=(int)(5*getResources().getDisplayMetrics().density); lp.setMargins(margin,margin,margin,margin);
   card.setLayoutParams(lp); card.setGravity(android.view.Gravity.CENTER); card.setTextSize(25); card.setTag(i); card.setId(View.generateViewId());
   card.setOnClickListener(v -> choose(index)); grid.addView(card);
  }
  findViewById(R.id.reset).setOnClickListener(v -> { game=new CardMatchingGame(16); hint=getString(R.string.initial_hint); render(); });
  findViewById(R.id.exit).setOnClickListener(v -> finish()); render();
 }
 private void choose(int index) {
  Card current=game.cardAtIndex(index), previous=null;
  if(current.isMatched()) return;
  for(int i=0;i<16;i++) { Card c=game.cardAtIndex(i); if(i!=index && c.isChosen() && !c.isMatched()) previous=c; }
  boolean wasChosen=current.isChosen(); game.chooseCardAtIndex(index);
  if(wasChosen) hint=getString(R.string.initial_hint);
  else if(previous==null) hint=getString(R.string.chosen_hint,current.toString());
  else hint=getString(current.isMatched()?R.string.match_hint:R.string.mismatch_hint,previous.toString(),current.toString());
  if(game.getMatchedCount()==16) hint=getString(R.string.complete,game.getScore());
  else if(game.isFinished()) hint=getString(R.string.unmatched_end,game.getScore());
  Player.best=Math.max(Player.best,game.getScore()); render();
 }
 private void render() {
  ((TextView)findViewById(R.id.score)).setText(getString(R.string.score,game.getScore()));
  ((TextView)findViewById(R.id.progress)).setText(getString(R.string.progress,game.getMatchedCount(),16));
  ((TextView)findViewById(R.id.hint)).setText(hint);
  for(int i=0;i<16;i++) { Card c=game.cardAtIndex(i); TextView v=cards[i]; boolean visible=c.isChosen() || c.isMatched();
   v.setText(visible?c.toString():getString(R.string.card_back));
   v.setBackgroundResource(c.isMatched()?R.drawable.card_matched:visible?R.drawable.card_front:R.drawable.card_back);
   v.setTextColor(getColor(!visible?R.color.paper:c.toString().startsWith(Card.Companion.getValidSuits()[0]) || c.toString().startsWith(Card.Companion.getValidSuits()[1])?R.color.red:R.color.ink));
   v.setEnabled(!c.isMatched()); v.setAlpha(c.isMatched()?0.65f:1f);
   v.setContentDescription(visible?getString(R.string.card_face,i+1,c.toString()):getString(R.string.card_hidden,i+1));
  }
 }
 @Override protected void onSaveInstanceState(Bundle out) { out.putSerializable(getString(R.string.key_model),game); out.putString(getString(R.string.key_hint),hint); super.onSaveInstanceState(out); }
 public CardMatchingGame getGameForTest() { return game; }
 public void setGameForTest(CardMatchingGame value) { game=value; hint=getString(R.string.initial_hint); render(); }
}
