Name Turno-matic

SetCompressor lzma

# Defines
!define REGKEY "SOFTWARE\$(^Name)"
!define VERSION 0.12.0.7
!define COMPANY "Turno-matic"
!define URL "http://is0708.googlecode.com"

# MUI defines
!define MUI_ICON Tema\installer.ico
!define MUI_UNICON Tema\uninstaller.ico
!define MUI_CUSTOMFUNCTION_GUIINIT CustomGUIInit
!define MUI_LANGDLL_REGISTRY_ROOT HKLM
!define MUI_LANGDLL_REGISTRY_KEY ${REGKEY}
!define MUI_LANGDLL_REGISTRY_VALUENAME InstallerLanguage

; MUI Settings / Header
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_RIGHT
!define MUI_HEADERIMAGE_BITMAP ".\Tema\header-r.bmp"
!define MUI_HEADERIMAGE_UNBITMAP ".\Tema\header-r-un.bmp"

; MUI Settings / Wizard
!define MUI_WELCOMEFINISHPAGE_BITMAP ".\Tema\wizard.bmp"
!define MUI_UNWELCOMEFINISHPAGE_BITMAP ".\Tema\wizard-un.bmp"

!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_LICENSEPAGE_RADIOBUTTONS
!define MUI_STARTMENUPAGE_REGISTRY_ROOT HKLM
!define MUI_STARTMENUPAGE_REGISTRY_KEY ${REGKEY}
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME StartMenuGroup
!define MUI_STARTMENUPAGE_DEFAULTFOLDER Turno-matic
!define MUI_STARTMENUPAGE
!define MUI_FINISHPAGE_SHOWREADME $INSTDIR\Ayuda\ES\index.html
!define MUI_FINISHPAGE_23
!define MUI_FINISHPAGE_RUN "$ACCESO_DIRECTO"
!define MUI_FINISHPAGE_RUN_PARAMETERS "$ATRIBUTOS"
!define MUI_UNFINISHPAGE_NOAUTOCLOSE

# Included files
#!include Sections.nsh
!include MUI.nsh

# Reserved Files
ReserveFile "${NSISDIR}\Plugins\BGImage.dll"
!insertmacro MUI_RESERVEFILE_LANGDLL
ReserveFile "${NSISDIR}\Plugins\AdvSplash.dll"

# Variables
Var StartMenuGroup
Var JAVA_HOME
Var JAVA_VER
Var JAVA_INSTALLATION_MSG
Var JAVA
Var ACCESO_DIRECTO
Var ATRIBUTOS

# Installer pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE Licencia.txt
#!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_STARTMENU Application $StartMenuGroup
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

# Installer languages
!insertmacro MUI_LANGUAGE Spanish
!insertmacro MUI_LANGUAGE English
!insertmacro MUI_LANGUAGE Polish


# Installer attributes
OutFile "Instalar Turno-matic.exe"
InstallDir $PROGRAMFILES\$(^Name)
CRCCheck on
XPStyle on
ShowInstDetails show
VIProductVersion ${VERSION}
VIAddVersionKey /LANG=${LANG_SPANISH} ProductName Turno-matic
VIAddVersionKey /LANG=${LANG_SPANISH} ProductVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_SPANISH} FileVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_SPANISH} FileDescription ""
VIAddVersionKey /LANG=${LANG_SPANISH} LegalCopyright ""
InstallDirRegKey HKLM "${REGKEY}" Path
ShowUninstDetails show

# Installer sections
# Instalar Java si necesario
Section "-Java 6" SEC0000
    inijava:
    IntCmp $JAVA 0 nojava6 java6 java6
    nojava6:
        MessageBox MB_OKCANCEL|MB_ICONQUESTION|MB_DEFBUTTON1 "Se va a instalar Java 6. Desea continuar?" IDOK njok IDCANCEL njcancel
        njok:
            ExecWait .\jre-6u5-windows-i586-p-s.exe
            call DetectarJava
            Goto inijava
        njcancel:
            Abort "Sin Java 6 o superior no se puede seguir con la instalacion."
            Goto njfin
        njfin:
        Goto finjava
    java6:
        #MessageBox MB_OK|MB_ICONINFORMATION|MB_DEFBUTTON1 "Java 6 detectado"
        DetailPrint "Java $JAVA_VER detectado en $ACCESO_DIRECTO"
        Goto finjava
    finjava:
SectionEnd

Section -jar SEC0001
    SetOutPath $INSTDIR
    SetOverwrite on
    File ..\IS0708\Turno-matic.jar
    File .\Tema\Icono.ico
    WriteRegStr HKLM "${REGKEY}\Components" jar 1
    SetOutPath $INSTDIR
    CreateShortCut "$INSTDIR\Turno-matic.lnk" "$ACCESO_DIRECTO" "$ATRIBUTOS" "$INSTDIR\Icono.ico"
SectionEnd

Section -Ayuda SEC0002
    SetOutPath $INSTDIR\Ayuda
    SetOverwrite on
    File /r ..\IS0708\Ayuda\*
    SetOutPath $INSTDIR
    CreateShortcut $INSTDIR\Ayuda.lnk $INSTDIR\Ayuda\ES\index.html
    WriteRegStr HKLM "${REGKEY}\Components" Ayuda 1
SectionEnd

Section -post SEC0003
    WriteRegStr HKLM "${REGKEY}" Path $INSTDIR
    SetOutPath $INSTDIR
    WriteUninstaller "$INSTDIR\Desinstalar Turno-matic.exe"
    !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
        SetOutPath $SMPROGRAMS\$StartMenuGroup
        CreateShortcut "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk" "$INSTDIR\Desinstalar Turno-matic.exe"
        SetOutPath $SMPROGRAMS\$StartMenuGroup
        CreateShortcut "$SMPROGRAMS\$StartMenuGroup\Ayuda.lnk "$INSTDIR\Ayuda\ES\index.html
        SetOutPath $SMPROGRAMS\$StartMenuGroup
        CreateShortCut "$SMPROGRAMS\$StartMenuGroup\Turno-matic.lnk" "$ACCESO_DIRECTO" "$ATRIBUTOS" "$INSTDIR\Icono.ico"
        SetOutPath $DESKTOP
        CreateShortCut "$DESKTOP\Turno-matic.lnk" "$ACCESO_DIRECTO" "$ATRIBUTOS" "$INSTDIR\Icono.ico"
    !insertmacro MUI_STARTMENU_WRITE_END
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayName "$(^Name)"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayVersion "${VERSION}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayIcon "$INSTDIR\Desinstalar Turno-matic.exe"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" UninstallString "$INSTDIR\Desinstalar Turno-matic.exe"
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoModify 1
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoRepair 1
SectionEnd

# Macro for selecting uninstaller sections
!macro SELECT_UNSECTION SECTION_NAME UNSECTION_ID
    Push $R0
    ReadRegStr $R0 HKLM "${REGKEY}\Components" "${SECTION_NAME}"
    StrCmp $R0 1 0 next${UNSECTION_ID}
    !insertmacro SelectSection "${UNSECTION_ID}"
    GoTo done${UNSECTION_ID}
next${UNSECTION_ID}:
    !insertmacro UnselectSection "${UNSECTION_ID}"
done${UNSECTION_ID}:
    Pop $R0
!macroend

# Uninstaller sections
Section /o -un.Ayuda UNSEC0002
    Delete /REBOOTOK $INSTDIR\Ayuda.lnk
    RmDir /r /REBOOTOK $INSTDIR\Ayuda
    DeleteRegValue HKLM "${REGKEY}\Components" Ayuda
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\Ayuda.lnk"
SectionEnd

Section /o -un.jar UNSEC0001
    Delete /REBOOTOK $INSTDIR\Turno-matic.jar
    Delete /REBOOTOK $INSTDIR\Icono.ico
    DeleteRegValue HKLM "${REGKEY}\Components" jar
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\Turno-matic.lnk"
    Delete /REBOOTOK "$INSTDIR\Turno-matic.lnk"
    Delete /REBOOTOK "$DESKTOP\Turno-matic.lnk"
SectionEnd

Section /o "-un.Java 6" UNSEC0000
    #Delete /REBOOTOK $INSTDIR\jre-6u5-windows-i586-p-s.exe
    #DeleteRegValue HKLM "${REGKEY}\Components" "Java 6"
SectionEnd

Section -un.post UNSEC0003
    DeleteRegKey HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk"
    Delete /REBOOTOK "$INSTDIR\Desinstalar Turno-matic.exe"
    DeleteRegValue HKLM "${REGKEY}" StartMenuGroup
    DeleteRegValue HKLM "${REGKEY}" Path
    DeleteRegKey /IfEmpty HKLM "${REGKEY}\Components"
    DeleteRegKey /IfEmpty HKLM "${REGKEY}"
    RmDir /REBOOTOK $SMPROGRAMS\$StartMenuGroup
    RmDir /REBOOTOK $INSTDIR
    Push $R0
    StrCpy $R0 $StartMenuGroup 1
    StrCmp $R0 ">" no_smgroup
no_smgroup:
    Pop $R0
SectionEnd

# Installer functions
Function CustomGUIInit
    Push $R1
    Push $R2
    BgImage::SetReturn /NOUNLOAD on
    BgImage::SetBg /NOUNLOAD /GRADIENT 255 255 255 255 255 255
    Pop $R1
    Strcmp $R1 success 0 error
    File /oname=$PLUGINSDIR\bgimage.bmp Tema\Turno-matic_bg.bmp
    System::call "user32::GetSystemMetrics(i 0)i.R1"
    System::call "user32::GetSystemMetrics(i 1)i.R2"
    IntOp $R1 $R1 - 1024
    IntOp $R1 $R1 / 2
    IntOp $R2 $R2 - 768
    IntOp $R2 $R2 / 2
    BGImage::AddImage /NOUNLOAD $PLUGINSDIR\bgimage.bmp $R1 $R2
    CreateFont $R1 "Times New Roman" 26 700 /ITALIC
    BGImage::AddText /NOUNLOAD "$(^SetupCaption)" $R1 64 128 128 16 8 500 100
    Pop $R1
    Strcmp $R1 success 0 error
    BGImage::Redraw /NOUNLOAD
    Goto done
error:
    MessageBox MB_OK|MB_ICONSTOP $R1
done:
    Pop $R2
    Pop $R1
FunctionEnd

Function .onGUIEnd
    BGImage::Destroy
FunctionEnd

Function .onInit

    # Comprueba que no se este ejecutando ya
    System::Call 'kernel32::CreateMutexA(i 0, i 0, t "myMutex") i .r1 ?e'
    Pop $R0
    StrCmp $R0 0 +3
    MessageBox MB_OK|MB_ICONEXCLAMATION "El instalador ya se esta ejecutando"
    Abort
    
    InitPluginsDir
    Push $R1
    File /oname=$PLUGINSDIR\spltmp.bmp Tema\Turno-matic_sp.bmp
    advsplash::show 1000 600 400 -1 $PLUGINSDIR\spltmp
    Call DetectarJava
    Pop $R1
    Pop $R1
    !insertmacro MUI_LANGDLL_DISPLAY
FunctionEnd

; Esta funcion sirve para comprobar la versión de la maquina virtual de java
Function DetectarJava
    Push $0
    Push $1
    ReadRegStr $JAVA_VER HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" CurrentVersion
    StrCmp "" "$JAVA_VER" JavaNotPresent CheckJavaVer

    JavaNotPresent:
        StrCpy $JAVA_INSTALLATION_MSG "Java Runtime Environment is not installed on your computer. You need version 1.6 or newer to run this program."
        IntOp $JAVA 0 + 0
        Goto Done

    CheckJavaVer:
        ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$JAVA_VER" JavaHome
        GetFullPathName /SHORT $JAVA_HOME "$0"
        StrCpy $0 $JAVA_VER 1 0
        StrCpy $1 $JAVA_VER 1 2
        StrCpy $JAVA_VER "$0$1"
        IntCmp 16 $JAVA_VER FoundCorrectJavaVer FoundCorrectJavaVer JavaVerNotCorrect

    FoundCorrectJavaVer:
        IfFileExists "$JAVA_HOME\bin\javaw.exe" 0 JavaNotPresent
        
        # Detecta donde la ruta de java
        GetFullPathName $0 "$JAVA_HOME\bin"
        StrCpy $0 "$JAVA_HOME\bin\javaw.exe"
        System::Call 'kernel32::GetLongPathName(t r0, t .r1, i ${NSIS_MAX_STRLEN}) i .r2'
        StrCmp $2 error +2
        StrCpy $0 $1
        StrCpy $ACCESO_DIRECTO "$0"
        StrCpy $ATRIBUTOS "-jar $\"$INSTDIR\Turno-matic.jar$\""
        
        IntOp $JAVA 1 + 0
        Goto Done

    JavaVerNotCorrect:
        StrCpy $JAVA_INSTALLATION_MSG "The version of Java Runtime Environment installed on your computer is $JAVA_VER. Version 1.6 or newer is required to run this program."
        IntOp $JAVA 0 + 0
        Goto Done

    Done:
        Pop $1
        Pop $0
FunctionEnd

# Uninstaller functions
Function un.onInit
    ReadRegStr $INSTDIR HKLM "${REGKEY}" Path
    !insertmacro MUI_STARTMENU_GETFOLDER Application $StartMenuGroup
    !insertmacro MUI_UNGETLANGUAGE
    !insertmacro SELECT_UNSECTION "Java 6" ${UNSEC0000}
    !insertmacro SELECT_UNSECTION jar ${UNSEC0001}
    !insertmacro SELECT_UNSECTION Ayuda ${UNSEC0002}
FunctionEnd

# Section Descriptions
#!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
#!insertmacro MUI_DESCRIPTION_TEXT ${SEC0000} $(SEC0000_DESC)
#!insertmacro MUI_FUNCTION_DESCRIPTION_END

# Installer Language Strings

LangString ^UninstallLink ${LANG_SPANISH} "Desinstalar $(^Name)"
LangString ^UninstallLink ${LANG_ENGLISH} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_POLISH} "Uninstall $(^Name)"

#LangString SEC0000_DESC ${LANG_SPANISH} "Java jre1.6 Update 5"
#LangString SEC0000_DESC ${LANG_ENGLISH} "Java jre1.6 Update 5"
#LangString SEC0000_DESC ${LANG_POLISH} "Java jre1.6 Update 5"
