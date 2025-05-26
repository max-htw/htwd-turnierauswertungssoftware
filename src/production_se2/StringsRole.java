public enum StringsRole {
    Admin, Team, Stranger;

    public  interface RoleTask{}

    enum AdminTasks implements RoleTask {
        Einstellungen, Turnierplan, Matchdetails, Status, Ergebnisse, Historie,  Hallo
    }

    enum TeamTasks implements RoleTask {
        Matchdetails, Overview, Turnierplan, Stand
    }

    enum KeineRoleTasks implements RoleTask{
        SelectRole
    }
}
