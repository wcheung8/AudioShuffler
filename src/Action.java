public class Action {
    static int count = 0;
    String name;
    int id;
    Runnable act;
    String keybind;

    public Action(String n, Runnable r) {
        this.name = n;
        this.act = r;
        this.id = count;
        count++;
        if (HotKeyInterface.prop.containsKey(n)) {
            String keycode = HotKeyInterface.prop.getProperty(n);
            HotKeyInterface.bind(this, keycode);
        } else {
            this.keybind = "";
        }
    }

    public String toString() {
        return name;
    }

}
