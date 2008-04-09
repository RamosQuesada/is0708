Name "Turno-matic Lab"

SetCompressor lzma

# Defines
!define REGKEY "SOFTWARE\$(^Name)"
!define VERSION 0.13.3.2
!define COMPANY "Turno-matic"
!define URL "http://is0708.googlecode.com"

# MUI defines
!define MUI_ICON Tema\installer.ico
!define MUI_UNICON Tema\uninstaller.ico
!define MUI_CUSTOMFUNCTION_GUIINIT CustomGUIInit

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
!define MUI_FINISHPAGE_SHOWREADME $INSTDIR\Ayuda\ES\index.html
!define MUI_FINISHPAGE_23
!define MUI_UNFINISHPAGE_NOAUTOCLOSE
!define MUI_FINISHPAGE_RUN "c:\jdk1.6.0_02\bin\javaw.exe"
!define MUI_FINISHPAGE_RUN_PARAMETERS "-jar $INSTDIR\Turno-matic.jar"

# Included files
!include Sections.nsh
!include MUI.nsh

# Reserved Files
ReserveFile "${NSISDIR}\Plugins\BGImage.dll"
ReserveFile "${NSISDIR}\Plugins\AdvSplash.dll"

# Installer pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE Licencia.txt
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

# Installer languages
!insertmacro MUI_LANGUAGE Spanish
!insertmacro MUI_LANGUAGE English
!insertmacro MUI_LANGUAGE Polish

# Installer attributes
OutFile "Instalar Turno-matic en lab.exe"
InstallDir "c:\hlocal\Turno-matic"
CRCCheck on
XPStyle on
ShowInstDetails show
VIProductVersion ${VERSION}
VIAddVersionKey /LANG=${LANG_SPANISH} ProductName "Turno-matic Lab"
VIAddVersionKey /LANG=${LANG_SPANISH} ProductVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_SPANISH} CompanyName "${COMPANY}"
VIAddVersionKey /LANG=${LANG_SPANISH} CompanyWebsite "${URL}"
VIAddVersionKey /LANG=${LANG_SPANISH} FileVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_SPANISH} FileDescription ""
VIAddVersionKey /LANG=${LANG_SPANISH} LegalCopyright ""

# Installer sections
Section -Main SEC0000
    #jar
    SetOutPath $INSTDIR
    SetOverwrite on
    File ..\IS0708\Turno-matic.jar
    File .\Tema\Icono.ico
    SetOutPath $INSTDIR
    CreateShortCut "$INSTDIR\Turno-matic.lnk" "c:\jdk1.6.0_02\bin\javaw.exe" "-jar $\"$INSTDIR\Turno-matic.jar$\"" "$INSTDIR\Icono.ico"
    
    #Ayuda
    SetOutPath $INSTDIR\Ayuda
    SetOverwrite on
    File /r ..\IS0708\Ayuda\*
    RmDir /r $INSTDIR\Ayuda\.svn
    RmDir /r $INSTDIR\Ayuda\ES\.svn
    RmDir /r $INSTDIR\Ayuda\Imagenes\.svn
    SetOutPath $INSTDIR
    CreateShortcut $INSTDIR\Ayuda.lnk $INSTDIR\Ayuda\ES\index.html
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
    Pop $R1
    Pop $R1
    !insertmacro MUI_LANGDLL_DISPLAY
FunctionEnd

