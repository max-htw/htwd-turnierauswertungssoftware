public class RoleAdmin_TaskEinstellungen_Renderer extends RoleWithTaskBase_Renderer<RoleAdmin_TaskEinstellungen_Data>{

  /*
  Mitteilung an das FrontEnd-Team:
  Diese Klasse bitte waehrend der Entwicklung unveraendert lassen.
  Die Entwicklungsarbeiten sollen in der Klasse "RoleAdmin_TaskEinstellungen_Renderer_Arbeitskopie"
  durchgefuehrt werden. Wenn das View fertig ist, kopieren wir die Code in diese Klasse.
  So koennen andere Entwickler, waehrend an der Arbeitskopie gearbeitet wird,
  mit der vereinfachte Version des Views weiterarbeiten.
   */

  @Override
  public RoleAdmin_TaskEinstellungen_Data getEmptyDaten() {
    RoleAdmin_TaskEinstellungen_Data ausgabe = new RoleAdmin_TaskEinstellungen_Data();
    ausgabe.htmlTitel = "Turnierkonfiguration";
    return  ausgabe;
  }

}
