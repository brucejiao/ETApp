package com.bruce.android.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.bruce.android.common.AppContext;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class SharedPreferences {

    private static final String SP_NAME = "souyue";
    public static final String KEY_LOGIN_TOKEN = "login_token";
    public static final String KEY_LOGIN_TYPE = "login_type";
    private final int ZERO = 0;

    private static SharedPreferences instance = new SharedPreferences();

    public SharedPreferences() {
    }

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new SharedPreferences();
        }
    }

    public static SharedPreferences getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    private android.content.SharedPreferences getSp() {
        return AppContext.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public int getInt(String key, int def) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getInt(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putInt(String key, int val) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putInt(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getLong(String key, long def) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getLong(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putLong(String key, long val) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putLong(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(String key, String def) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getString(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putString(String key, String val) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putString(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String key, boolean def) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getBoolean(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putBoolean(String key, boolean val) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putBoolean(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void putFloat(String key, float value) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putFloat(key, value);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float getFloat(String key, float defaultValue) {
        float value = ZERO;
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                value = sp.getFloat(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    public void putStringSet(String key, Set<String> value) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putStringSet(key, value);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        Set<String> set = null;
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
            set = sp.getStringSet(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }


    /**
     * 添加对象
     *
     * @param key
     * @param t
     */
    public <T> void putModel(String key, T t) {
        if (!TextUtils.isEmpty(key) && t != null) {
            putString(key, JSON.toJSONString(t));// fastjson
        }
    }

    /**
     * 获取对象
     *
     * @param key
     * @param clazz
     * @return
     */
    public <T> T getModel(String key, Class<T> clazz) {
        String value = null;
        if (!TextUtils.isEmpty(key)) {
            value = getString(key,"");
        }
        return TextUtils.isEmpty(value) ? null : JSON.parseObject(value, clazz);// fastjson
    }

    /**
     * 添加集合
     *
     * @param key
     * @param t
     */
    public <T> void putModels(String key, List<T> t) {
        if (!TextUtils.isEmpty(key) && t != null && t.size() > ZERO) {
            putString(key, JSON.toJSONString(t));// fastjson
        }
    }

    /**
     * 获取集合
     *
     * @param key
     * @param clazz
     * @return
     */
    public <T> List<T> getModels(String key, Class<T> clazz) {
        String value = null;
        if (!TextUtils.isEmpty(key)) {
            value = getString(key,"");
        }
        return TextUtils.isEmpty(value) ? null : JSON.parseArray(value, clazz);// fastjson
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                return sp.contains(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public void remove(String key) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.remove(key);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.clear();
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public Map<String, ?> getAll() {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                return sp.getAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日志输出所有键值对
     */
    public void selectKeyAll() {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Map<String, Object> map = (Map<String, Object>) sp.getAll();
                for (String key : map.keySet()) {
                    LogUtil.i("======share======", "key= " + key + " and value= " + map.get(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
