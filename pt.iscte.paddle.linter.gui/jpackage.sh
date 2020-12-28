jpackage \
--name Sprinter \
--module-path lib \
-m pt.iscte.paddle.linter.gui/pt.iscte.paddle.linter.gui.main.LinterDemoGUI \
--add-modules pt.iscte.paddle.linter.gui \
--type dmg \
-d out \
--verbose \
--java-options '--enable-preview -XstartOnFirstThread'