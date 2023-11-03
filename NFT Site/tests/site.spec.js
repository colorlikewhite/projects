const { test, expect } = require('@playwright/test');

const websiteURL = 'http://localhost:3000';

// Expected constants
const expectedTitle = 'KRYPTO';
const expectedLogoText = 'KRYPTO';
const expectedMenuItemCount = 4;
const expectedHeroTitle = 'Discover and Collect Rare NFTs';
const expectedHeroSubText = 'The most secure marketplace for buying and selling unique crypto assets.';
const expectedHeroLinkCount = 2;
const expectedFeaturedTitle = 'Featured In:';
const expectedFeaturedImageCount = 4;
const expectedAboutTitle = 'Built-In Analytics to Track Your NFTs';
const expectedBuyTitle = 'Built-In Analytics to Track Your NFTs';
const expectedTestimonialsTitle = 'Testimonials';
const expectedTestimonialCount = 3;
const expectedReadyTitle = 'Are you ready?';
const expectedReadyLinkCount = 1;
const expectedMetaDescription = 'This is an example NFT website coded from a Figma tutorial';
const expectedMetaKeywordsNotEmpty = '';
const expectedNavs = ['Home', 'About', 'Services', 'Contact'];
const expectedAlts = ['Featured in fcompany', 'Featured in forbes', 'Featured in mit', 'Featured in tcrunch'];
const expectedNames = ['Olivia Cole', 'Evan White', 'Jessica Page'];
const expectedFooterLinks = ['Home', 'Buy NFTs', 'Sell NFTs', 'Browse NFTs', 'Email', 'LinkedIn', 'Instagram', 'Twitter', 'Market', 'Contact'];
const expectedIcons = ['fas fa-home', 'fas fa-money-bill-wave', 'fas fa-hand-holding-usd', 'fas fa-eye', 'fas fa-envelope', 'fab fa-linkedin', 'fab fa-instagram', 'fab fa-twitter', 'fas fa-store', 'fas fa-phone'];
const expectedSubmitButtonText = 'Submit';

test.beforeEach(async ({ page }) => {
  await page.goto(websiteURL);
});

test('Check Page Title', async ({ page }) => {
  const title = await page.title();
  expect(title).toBe(expectedTitle);
});

test('Check Logo in Header', async ({ page }) => {
  const logoText = await page.locator('.logo').textContent();
  expect(logoText).toBe(expectedLogoText);
});

test('Check Navigation Menu in Header', async ({ page }) => {
  const menuItemCount = await page.locator('.menu .menu-item').count();
  expect(menuItemCount).toBe(expectedMenuItemCount);
});

test('Check Hero Section', async ({ page }) => {
  expect(await page.locator('.hero h1').textContent()).toBe(expectedHeroTitle);
  expect(await page.locator('.hero .sub-text').textContent()).toBe(expectedHeroSubText);
});

test('Check Links in Hero Section', async ({ page }) => {
  const linkCount = await page.locator('.hero .hero-text a').count();
  expect(linkCount).toBe(expectedHeroLinkCount);
});

test('Check Featured Section', async ({ page }) => {
  expect(await page.locator('.featured h2').textContent()).toBe(expectedFeaturedTitle);
  const imageCount = await page.locator('.featured .image-container img').count();
  expect(imageCount).toBe(expectedFeaturedImageCount);
});

test('Check About Section', async ({ page }) => {
  expect(await page.locator('#about h2').textContent()).toBe(expectedAboutTitle);
});

test('Check Buy Section', async ({ page }) => {
  expect(await page.locator('#buy h2').textContent()).toBe(expectedBuyTitle);
});

test('Check Testimonials Section', async ({ page }) => {
  expect(await page.locator('.testimonials-header h2').textContent()).toBe(expectedTestimonialsTitle);
  const testimonialCount = await page.locator('.testimonial-cards .testimonial').count();
  expect(testimonialCount).toBe(expectedTestimonialCount);
});

test('Check Ready Section', async ({ page }) => {
  expect(await page.locator('.ready h2').textContent()).toBe(expectedReadyTitle);
  const readyLinkCount = await page.locator('.ready a').count();
  expect(readyLinkCount).toBe(expectedReadyLinkCount);
});

test('Check Footer', async ({ page }) => {
  const footerLinkCount = await page.locator('.footer-link').count();
  expect(footerLinkCount).toBeGreaterThan(0);
});

test('Check SEO Meta Description', async ({ page }) => {
  const metaDescription = await page.getAttribute('meta[name="description"]', 'content');
  expect(metaDescription).toBe(expectedMetaDescription);
});

test('Check SEO Meta Keywords', async ({ page }) => {
  const metaKeywords = await page.getAttribute('meta[name="keywords"]', 'content');
  expect(metaKeywords).not.toBe(expectedMetaKeywordsNotEmpty);
});

test('Check Newsletter Form Elements', async ({ page }) => {
  const newsletterForm = await page.locator('.newsletter-form');
  const inputField = await newsletterForm.locator('input[type="email"]');
  const submitButton = await newsletterForm.locator('input[type="submit"]');
  expect(await inputField.isVisible()).toBe(true);
  expect(await submitButton.isVisible()).toBe(true);
  expect(await submitButton.getAttribute('value')).toBe(expectedSubmitButtonText);
});

test('Check All Navigation Links', async ({ page }) => {
  const navLinks = await page.locator('.menu-item');
  const count = await navLinks.count();

  for (let i = 0; i < count; i++) {
    const link = navLinks.nth(i);
    expect(await link.isVisible()).toBe(true);
  }
});


test('Check All Featured Company Logos', async ({ page }) => {
  const images = await page.locator('.image-container img');
  const count = await images.count();

  for (let i = 0; i < count; i++) {
    const image = images.nth(i);
    expect(await image.isVisible()).toBe(true);
  }
});


test('Check All Testimonials', async ({ page }) => {
  const testimonials = await page.locator('.testimonial');
  const count = await testimonials.count();

  for (let i = 0; i < count; i++) {
    const testimonial = testimonials.nth(i);
    expect(await testimonial.isVisible()).toBe(true);
  }
});

test('Check All Footer Links', async ({ page }) => {
  const footerLinks = await page.locator('.footer-link');
  const count = await footerLinks.count();

  for (let i = 0; i < count; i++) {
    const link = footerLinks.nth(i);
    expect(await link.isVisible()).toBe(true);
  }
});

test('Check All Footer Icons', async ({ page }) => {
  await page.waitForResponse(response => 
    response.url().includes('font-awesome') && 
    response.status() === 200
  );
  const footerIcons = await page.locator('.footer-link i');
  const count = await footerIcons.count();

  for (let i = 0; i < count; i++) {
    const icon = footerIcons.nth(i);
    expect(await icon.isVisible()).toBe(true);
  }
});


