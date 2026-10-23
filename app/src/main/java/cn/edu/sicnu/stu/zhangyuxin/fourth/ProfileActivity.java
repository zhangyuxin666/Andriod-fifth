package cn.edu.sicnu.stu.zhangyuxin.fourth;
import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import java.util.ArrayList;
public class ProfileActivity extends Activity {
 private EditText name,id,phone,email;
 @Override public void onCreate(Bundle b) { super.onCreate(b); Player.init(this); setContentView(R.layout.activity_profile);
  name=findViewById(R.id.input_name); id=findViewById(R.id.input_student_id); phone=findViewById(R.id.input_phone); email=findViewById(R.id.input_email);
  name.setText(Player.name); id.setText(Player.id);
  if(!Player.phone.equals(getString(R.string.empty_value))) phone.setText(Player.phone);
  if(!Player.email.equals(getString(R.string.empty_value))) email.setText(Player.email);
  ((RadioGroup)findViewById(R.id.gender_group)).check(Player.gender.equals(getString(R.string.female))?R.id.female:R.id.male);
  for(int check:new int[]{R.id.travel,R.id.reading,R.id.gaming,R.id.shopping}) { CheckBox box=findViewById(check); box.setChecked(Player.hobbies.contains(box.getText())); }
  findViewById(R.id.cancel).setOnClickListener(v -> finish()); findViewById(R.id.save).setOnClickListener(v -> save());
 }
 private void save() {
  if(name.getText().toString().trim().isEmpty() || id.getText().toString().trim().isEmpty()) { name.setError(getString(R.string.required)); return; }
  String p=phone.getText().toString().trim(), e=email.getText().toString().trim();
  if(!p.isEmpty() && !android.util.Patterns.PHONE.matcher(p).matches()) { phone.setError(getString(R.string.invalid_phone)); return; }
  if(!e.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches()) { email.setError(getString(R.string.invalid_email)); return; }
  Player.name=name.getText().toString().trim(); Player.id=id.getText().toString().trim();
  Player.phone=p.isEmpty()?getString(R.string.empty_value):p; Player.email=e.isEmpty()?getString(R.string.empty_value):e;
  Player.gender=getString(((RadioGroup)findViewById(R.id.gender_group)).getCheckedRadioButtonId()==R.id.female?R.string.female:R.string.male);
  ArrayList<String> selected=new ArrayList<>();
  for(int check:new int[]{R.id.travel,R.id.reading,R.id.gaming,R.id.shopping}) { CheckBox box=findViewById(check); if(box.isChecked()) selected.add(box.getText().toString()); }
  Player.hobbies=selected.isEmpty()?getString(R.string.empty_value):android.text.TextUtils.join(getString(R.string.hobby_separator),selected);
  Toast.makeText(this,R.string.saved,Toast.LENGTH_SHORT).show(); finish();
 }
}
