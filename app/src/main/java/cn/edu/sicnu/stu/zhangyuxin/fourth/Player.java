package cn.edu.sicnu.stu.zhangyuxin.fourth;
final class Player {
 static String name, id, gender, phone, email, hobbies;
 static int best;
 static void init(android.content.Context c) {
  if(name != null) return;
  name=c.getString(R.string.default_name); id=c.getString(R.string.default_id);
  gender=c.getString(R.string.male); phone=c.getString(R.string.empty_value); email=c.getString(R.string.empty_value);
  hobbies=c.getString(R.string.reading)+c.getString(R.string.hobby_separator)+c.getString(R.string.gaming);
 }
}
