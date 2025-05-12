import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;


public class WebserverSwing_Handler extends JFrame implements ActionListener, RoleWithTaskBase_Renderer.ActionStringGenerator {

  private JButton btnSearch;
  private JPanel panelViewContainer;
  private  JLabel msgLabel;


  public  WebserverSwing_Handler(String title){
    super(title);


    setLocation(300, 50);
    setSize(800, 400);

    JPanel pnMain = new JPanel();
    pnMain.setSize(this.getSize().width-5, this.getSize().height-20);
    pnMain.setLayout(new BoxLayout(pnMain,BoxLayout.Y_AXIS));

    JPanel pn1 = new JPanel();
    pn1.setLayout(new GridLayout(0, 1));
    pn1.setSize(this.getSize().width-5, 20);
    pn1.add(new JLabel("Teammame: <teamName>"));
    pn1.add(new JLabel("Gruppenname: <groupName>"));
    msgLabel = new JLabel();
    JButton tmpNavButton = new JButton("Team Turnierplan aufrufen");

    tmpNavButton.setActionCommand(generateActionString(
                       new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
                                  StringsRole.Team, StringsRole.TeamTasks.Turnierplan,2,1)
    ));

    /*
    tmpNavButton.setActionCommand(generateActionString(
      new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
        StringsRole.Admin, StringsRole.AdminTasks.Einstellungen,-1,-1)
    ));

     */
    tmpNavButton.addActionListener(this);
    pn1.add(msgLabel);
    pn1.add(tmpNavButton);
    pnMain.add(pn1);

    panelViewContainer = new JPanel();
    panelViewContainer.setLayout(new BorderLayout());
    panelViewContainer.setSize(this.getSize().width-5, 20);


    pnMain.add(panelViewContainer);
    this.add(pnMain);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    pn1.setVisible(true);
    panelViewContainer.setVisible(true);
    pnMain.setVisible(true);
    this.setVisible(true);
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    msgLabel.setText("Command: " + e.getActionCommand());
    StringsRole role = getRoleFromCommand(e.getActionCommand());
    StringsRole.RoleTask task = getTaskFromCommand(e.getActionCommand());
    int group = getGroupNrFromCommand(e.getActionCommand());
    int team = getTeamNrFromCommand(e.getActionCommand());
    Map<String, String> map = getQsFromCommand(e.getActionCommand());

    RequestVerteiler v = new RequestVerteiler(role, task, group, team, map);

    JPanel out = v.getResponseJPanel(getWidth(), getHeight(), this, this);
    setView(out);
  }


  public void setView(JPanel panel){
    this.panelViewContainer.removeAll();
    this.panelViewContainer.add(panel);
    this.repaint();
  }

  public StringsRole getRoleFromCommand(String cmd){
    StringsRole out = StringsRole.Stranger;
    String[] parts = cmd.split(";");
    if(parts.length > 0){
      if(parts[0].equals(StringsRole.Admin.name().toLowerCase())){
        out = StringsRole.Admin;
      }
      else if(
        parts[0].length() == 2 &&
        parts[0].charAt(0) >= '0' &&
        parts[0].charAt(0) <= '9' &&
        parts[0].charAt(1) >= 'a' &&
        parts[0].charAt(1) <= 'h'){

        out = StringsRole.Team;
      }
    }
    return out;
  }

  public StringsRole.RoleTask getTaskFromCommand(String cmd){
    String[] parts = cmd.split(";");
    StringsRole.RoleTask out = StringsRole.KeineRoleTasks.SelectRole;

    StringsRole.RoleTask[] tasks = {};

    if(parts.length > 1){
      if(getRoleFromCommand(cmd) == StringsRole.Admin) {
        tasks = StringsRole.AdminTasks.values();
      }
      else if(getRoleFromCommand(cmd) == StringsRole.Team){
        tasks = StringsRole.TeamTasks.values();
      }
    }

    for (StringsRole.RoleTask rt : tasks) {
      if (parts[1].equals(rt.toString().toLowerCase())) {
        out = rt;
        break;
      }
    }

    return out;
  }

  public int getGroupNrFromCommand(String cmd){
    StringsRole role = getRoleFromCommand(cmd);

    if(role == StringsRole.Team) {
      String[] parts = cmd.split(";");

      MyHelpers.IntPair ip = new MyHelpers.IntPair(-1, -1);
      if (parts.length > 0) {
        ip = MyHelpers.GroupIDandTeamIDfromBezeichnung(parts[0]);
      }
      return ip.x;
    }
    else{
      return -1;
    }
  }


  public int getTeamNrFromCommand(String cmd){
    StringsRole role = getRoleFromCommand(cmd);

    if(role == StringsRole.Team) {
      String[] parts = cmd.split(";");

      MyHelpers.IntPair ip = new MyHelpers.IntPair(-1, -1);
      if (parts.length > 0) {
        ip = MyHelpers.GroupIDandTeamIDfromBezeichnung(parts[0]);
      }

      return ip.y;
    }
    else
      return -1;
  }


  public Map<String, String> getQsFromCommand(String cmd){
    return new HashMap<String, String>();
  }

  @Override
  public String generateActionString(RoleWithTaskBase_Renderer.ActionForRoleAndTask action) {
    if (action == null)
      return "todo;WebserverSwing_Handelr;generateActionString_for_null";
    String roleStr;
    if(action.role == StringsRole.Team){
        roleStr = MyHelpers.TeamBezeichnung(action.groupID, action.teamID);
    }
    else{
        roleStr = action.role.name().toLowerCase();
    }

    StringBuilder out = new StringBuilder(roleStr + ";" + action.task.toString().toLowerCase());
    for(StringsActions a: action.parameters.keySet()){
      out.append(";").append(action.parameters.get(a));
    }

    return out.toString();
  }

  //TODO: implement asynchronous updating of the gui thread
  // with help of invokeLater() and an instance of this class as an argument.
  public class SwingUpdateRequest implements Runnable
  {
    private StringsRole          _role;
    private StringsRole.RoleTask _task;
    Map<String, String>          _params;
    private WebserverSwing_Handler _outerJFrameClass;

    public SwingUpdateRequest(StringsRole role, StringsRole.RoleTask task, Map<String, String> params) {
      this._role = role;
      this._task = task;
      this._params = params;
      this._outerJFrameClass = WebserverSwing_Handler.this;
    }

    public void run() {
      RequestVerteiler v = new RequestVerteiler(_role, _task, 0, 0, _params);
      _outerJFrameClass.setView(v.getResponseJPanel(getWidth(), getHeight(),
        WebserverSwing_Handler.this,
        WebserverSwing_Handler.this));
    }
  }

}
