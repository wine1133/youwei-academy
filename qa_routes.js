const { chromium } = require('playwright-core');
(async () => {
  const browser = await chromium.launch({ executablePath: 'C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe', headless: true });
  const page = await browser.newPage({ viewport: { width: 1440, height: 1000 } });
  const errors = [];
  page.on('pageerror', e => errors.push('PAGE: ' + e.message));
  page.on('console', m => { if (m.type() === 'error') errors.push('CONSOLE: ' + m.text()); });
  await page.goto('http://localhost:9090/login', { waitUntil: 'networkidle' });
  const admin = {id:1,username:'admin',name:'管理员',role:'ADMIN',avatar:'/images/avatars/admin.svg'};
  await page.evaluate(u => localStorage.setItem('system-user', JSON.stringify(u)), admin);
  const manager = ['/manager/home','/manager/message','/manager/book','/manager/borrow','/manager/article','/manager/comments','/manager/homework','/manager/submit','/manager/user','/manager/teacher','/manager/person','/manager/password'];
  const results = [];
  for (const path of manager) {
    const before = errors.length;
    await page.goto('http://localhost:9090' + path, { waitUntil: 'networkidle' });
    await page.waitForTimeout(500);
    results.push({path, text: (await page.locator('body').innerText()).length, errors: errors.length - before});
  }
  const user = {id:2,username:'ill',name:'沈星河',role:'USER',avatar:'/images/avatars/student.svg'};
  await page.evaluate(u => localStorage.setItem('system-user', JSON.stringify(u)), user);
  const front = ['/front/home','/front/map','/front/message','/front/library','/front/book/1','/front/reader/1','/front/community','/front/article/1','/front/publish','/front/homework','/front/person','/front/password'];
  for (const path of front) {
    const before = errors.length;
    await page.goto('http://localhost:9090' + path, { waitUntil: 'networkidle' });
    await page.waitForTimeout(400);
    results.push({path, text: (await page.locator('body').innerText()).length, errors: errors.length - before});
  }
  await page.evaluate(() => localStorage.removeItem('system-user'));
  await page.goto('http://localhost:9090/manager/home', { waitUntil: 'networkidle' });
  results.push({path:'/manager/home(no-login)', text:(await page.locator('body').innerText()).length, url:page.url()});
  console.log(JSON.stringify({results, errors}, null, 2));
  await browser.close();
})();
