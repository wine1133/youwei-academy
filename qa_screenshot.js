const { chromium } = require('playwright-core');
(async () => {
  const edge = 'C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe';
  const browser = await chromium.launch({ executablePath: edge, headless: true });
  const page = await browser.newPage({ viewport: { width: 1440, height: 1000 } });
  await page.goto('http://localhost:9090/login', { waitUntil: 'networkidle' });
  await page.evaluate(() => localStorage.setItem('system-user', JSON.stringify({id:1,username:'admin',name:'管理员',role:'ADMIN',avatar:'/images/avatars/admin.svg'})));
  await page.goto('http://localhost:9090/manager/home', { waitUntil: 'networkidle' });
  await page.waitForTimeout(2500);
  await page.screenshot({ path: 'D:/BaiduNetdiskDownload/武哥带小白2026资料/manager-home-preview.png', fullPage: true });
  console.log(await page.title());
  await browser.close();
})();
